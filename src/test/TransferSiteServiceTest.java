package freecycling.example.freecycling.Core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import freecycling.example.freecycling.Interface.DTOs.TransferSiteDTO;

@ExtendWith(MockitoExtension.class)
public class TransferSiteServiceTest {

    @Mock
    TransferSiteRepository transferSiteRepository;

    @InjectMocks
    TransferSiteService transferSiteService;

    private TransferSite testSite;
    private TransferSiteDTO testSiteDTO;

    @BeforeEach
    public void setUp() {
        testSite = new TransferSite();
        testSite.setIdTransferSite(1);
        testSite.setTransferSiteName("Main Library");
        testSite.setTransferAddress("123 Main St");

        testSiteDTO = new TransferSiteDTO();
        testSiteDTO.setTransferSiteName("Main Library");
        testSiteDTO.setTransferAddress("123 Main St");
    }

    // Test save
    @Test
    public void testSave() {
        transferSiteService.save(testSiteDTO);
        verify(transferSiteRepository, times(1)).save(any(TransferSite.class));
    }

    // Test save maps fields correctly
    @Test
    public void testSaveMapsFields() {
        transferSiteService.save(testSiteDTO);
        verify(transferSiteRepository).save(argThat(site ->
            site.getTransferSiteName().equals("Main Library") &&
            site.getTransferAddress().equals("123 Main St")
        ));
    }

    // Test findById - site exists
    @Test
    public void testFindByIdExists() {
        when(transferSiteRepository.findById(1)).thenReturn(Optional.of(testSite));
        TransferSite result = transferSiteService.findById(1);
        assertNotNull(result);
        assertEquals("Main Library", result.getTransferSiteName());
    }

    // Test findById - site does not exist
    @Test
    public void testFindByIdNotFound() {
        when(transferSiteRepository.findById(999)).thenReturn(Optional.empty());
        TransferSite result = transferSiteService.findById(999);
        assertNull(result);
    }

    // Test findAll
    @Test
    public void testFindAll() {
        List<TransferSite> sites = new ArrayList<>();
        sites.add(testSite);
        when(transferSiteRepository.findAll()).thenReturn(sites);
        List<TransferSite> results = transferSiteService.findAll();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Main Library", results.get(0).getTransferSiteName());
    }

    // Test findByName - site exists
    @Test
    public void testFindByNameExists() {
        List<TransferSite> sites = new ArrayList<>();
        sites.add(testSite);
        when(transferSiteRepository.findByTransferSiteName("Main Library")).thenReturn(sites);
        List<TransferSite> results = transferSiteService.findByName("Main Library");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Main Library", results.get(0).getTransferSiteName());
    }

    // Test findByName - site does not exist
    @Test
    public void testFindByNameNotFound() {
        when(transferSiteRepository.findByTransferSiteName("Unknown")).thenReturn(new ArrayList<>());
        List<TransferSite> results = transferSiteService.findByName("Unknown");
        assertNotNull(results);
        assertEquals(0, results.size());
    }
}