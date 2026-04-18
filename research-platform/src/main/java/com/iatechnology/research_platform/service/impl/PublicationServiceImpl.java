package com.iatechnology.research_platform.service.impl;

import com.iatechnology.research_platform.dto.PublicationRequest;
import com.iatechnology.research_platform.dto.PublicationResponse;
import com.iatechnology.research_platform.entity.Chercheur;
import com.iatechnology.research_platform.entity.Publication;
import com.iatechnology.research_platform.entity.StatutPublication;
import com.iatechnology.research_platform.repository.ChercheurRepository;
import com.iatechnology.research_platform.repository.PublicationRepository;
import com.iatechnology.research_platform.service.impl.GroqService;
import com.iatechnology.research_platform.service.impl.PdfService;
import com.iatechnology.research_platform.service.PublicationService;
import com.iatechnology.research_platform.service.impl.SupabaseStorageService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private ChercheurRepository chercheurRepository;

    @Autowired
    private SupabaseStorageService supabaseStorageService;
    @Autowired
    private PdfService pdfService;

    @Autowired
    private GroqService groqService;


    // ─── Mapping helpers ────────────────────────────────────────────────────

    private PublicationResponse toResponse(Publication p) {
        PublicationResponse dto = new PublicationResponse();
        dto.setId(p.getId());
        dto.setTitre(p.getTitre());
        dto.setResume(p.getResume());
        dto.setMotsCles(p.getMotsCles());
        dto.setDoi(p.getDoi());
        dto.setFichierPdf(p.getFichierPdf());
        dto.setDatePublication(p.getDatePublication());
        dto.setStatut(p.getStatut());
        dto.setMettreEnAvant(p.isMettreEnAvant());

        // Map each chercheur to a brief summary — breaks the circular reference
        if (p.getChercheurs() != null) {
            List<PublicationResponse.ChercheurBriefDTO> briefs = p.getChercheurs()
                .stream()
                .map(c -> {
                    PublicationResponse.ChercheurBriefDTO brief =
                        new PublicationResponse.ChercheurBriefDTO();
                    brief.setId(c.getId());
                    brief.setNom(c.getNom());
                    brief.setPrenom(c.getPrenom());
                    brief.setDomaineNom(
                        c.getDomaine() != null ? c.getDomaine().getNom() : null
                    );
                    return brief;
                })
                .collect(Collectors.toList());
            dto.setChercheurs(briefs);
        }

        return dto;
    }

    // Resolve chercheur IDs to actual entities
    private List<Chercheur> resolveChercheurs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(
                "Une publication doit être associée à au moins un chercheur.");
        }

        List<Chercheur> chercheurs = new ArrayList<>();
        for (Long id : ids) {
            Chercheur c = chercheurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "Chercheur non trouvé avec l'id: " + id));
            chercheurs.add(c);
        }
        return chercheurs;
    }

    // ─── Public use cases ───────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> findAll() {
        return publicationRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    // Spec: public homepage shows published work only
    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> findPublished() {
        return publicationRepository.findByStatut(StatutPublication.PUBLIE)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationResponse findById(Long id) {
        Publication publication = publicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Publication non trouvée avec l'id: " + id));
        return toResponse(publication);
    }

    // Spec: "Recherche par mots-clés"
    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> searchByMotsCles(String motsCles) {
        return publicationRepository
            .findByTitreContainingIgnoreCaseOrMotsClesContainingIgnoreCaseOrResumeContainingIgnoreCase(
                motsCles, motsCles, motsCles)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    // Spec: find all publications of a given chercheur
    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> findByChercheur(Long chercheurId) {
        chercheurRepository.findById(chercheurId)
            .orElseThrow(() -> new RuntimeException(
                "Chercheur non trouvé avec l'id: " + chercheurId));

        return publicationRepository.findByChercheurs_Id(chercheurId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    // Spec: "Recherche par domaine" — find publications linked to a domain
    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> findByDomaine(Long domaineId) {
        return publicationRepository.findByChercheurs_Domaine_Id(domaineId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    // Public access — for homepage highlighted publications
    @Override
    @Transactional(readOnly = true)
    public List<PublicationResponse> findHighlighted() {
        return publicationRepository.findByMettreEnAvantTrueAndStatut(StatutPublication.PUBLIE)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    // ─── Admin only use cases ────────────────────────────────────────────────

    // Spec: "Ajout" — "Association à un ou plusieurs chercheurs"

@Override
public PublicationResponse create(PublicationRequest request) {

    if (request.getFichierPdf() == null || request.getFichierPdf().isEmpty()) {
        throw new RuntimeException("PDF obligatoire");
    }

    Publication publication = new Publication();
    publication.setTitre(request.getTitre());
    publication.setDoi(request.getDoi());

    String text = pdfService.extractText(request.getFichierPdf());

    String summary = groqService.summarizeLargeText(text);

    List<String> keywords = groqService.extractKeywords(summary);

    publication.setResume(summary);
    publication.setMotsCles(keywords);

   /*  try {
        List<byte[]> images =
                pdfService.extractImages(request.getFichierPdf());

        List<String> imageSummaries = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (byte[] img : images) {

                String imgSummary = groqService.summarizeImage(img);
                imageSummaries.add(imgSummary);
            }
        }

        // Optional: merge image insights into resume
        if (!imageSummaries.isEmpty()) {
            summary += "\n\nImage Analysis:\n" + String.join("\n", imageSummaries);
            publication.setResume(summary);
        }

    } catch (Exception e) {
        System.out.println("Image extraction skipped: " + e.getMessage());
    }*/
    try {
        String doi = request.getDoi();

        publication.setFichierPdf(
            supabaseStorageService.uploadFile(request.getFichierPdf(), doi)
        );

    } catch (IOException e) {
        throw new RuntimeException("Erreur upload PDF", e);
    }

    // ==============================
    // 6. METADATA
    // ==============================
    publication.setDatePublication(request.getDatePublication());
    publication.setStatut(StatutPublication.EN_ATTENTE);
    publication.setMettreEnAvant(request.isMettreEnAvant());

    publication.setChercheurs(resolveChercheurs(request.getChercheurIds()));

    return toResponse(publicationRepository.save(publication));
}

    // Spec: "Modification"

   @Override
    public PublicationResponse update(Long id, PublicationRequest request) {

        Publication publication = publicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Publication non trouvée avec l'id: " + id));

        // Update basic fields
        publication.setTitre(request.getTitre());
        publication.setResume(request.getResume());
        publication.setDoi(request.getDoi());
        publication.setDatePublication(request.getDatePublication());
        publication.setMettreEnAvant(request.isMettreEnAvant());

        // ✅ If new PDF is uploaded → replace it
        if (request.getFichierPdf() != null && !request.getFichierPdf().isEmpty()) {
            try {
                String fileName = request.getFichierPdf().getOriginalFilename();

                fileName = fileName
                .replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                String fileUrl = supabaseStorageService.uploadFile(
                    request.getFichierPdf(),
                    fileName
                );

                publication.setFichierPdf(fileUrl);

                // OPTIONAL: reprocess AI (recommended)
                String text = pdfService.extractText(request.getFichierPdf());
                String summary = groqService.summarizeLargeText(text);
                List<String> keywords = groqService.extractKeywords(summary);

                publication.setResume(summary);
                publication.setMotsCles(keywords);

            } catch (IOException e) {
                throw new RuntimeException("Erreur upload PDF", e);
            }
        }

        // ✅ Update chercheurs if provided
        if (request.getChercheurIds() != null && !request.getChercheurIds().isEmpty()) {
            publication.setChercheurs(resolveChercheurs(request.getChercheurIds()));
        }

        return toResponse(publicationRepository.save(publication));
    }

    // Spec: "Suppression"
    @Override
    public void delete(Long id) {
        if (!publicationRepository.existsById(id)) {
            throw new RuntimeException("Publication non trouvée avec l'id: " + id);
        }
        publicationRepository.deleteById(id);
    }

    // Admin manages publication workflow: EN_ATTENTE → PUBLIE → ARCHIVE
    @Override
    public PublicationResponse changeStatut(Long id, StatutPublication statut) {
        Publication publication = publicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Publication non trouvée avec l'id: " + id));
        publication.setStatut(statut);
        return toResponse(publicationRepository.save(publication));
    }
}