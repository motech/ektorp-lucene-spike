package repository;

import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import domain.Patient;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

//Runs on top of benerator generated data. Run as "mvn verify"
public class EktorpLucenePerformanceIT {
    private LuceneAwareCouchDbConnector dbConnector;
    private PatientRepository patientRepository;

    @Before
    public void setUp() throws Exception {
        HttpClient httpClient = new StdHttpClient.Builder().build();
        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        dbConnector = new LuceneAwareCouchDbConnector("patients", dbInstance);
        dbConnector.createDatabaseIfNotExists();
        patientRepository = new PatientRepository(dbConnector);
    }


    @Test
    public void shouldSearchForPatients(){
        Map<String, String> queryParams = new HashMap();
        queryParams.put("district", "Jehanabad");
        //patientRepository.add(createPatient("1", "we", 1, LocalDate.now()));
        List<Patient> patients = patientRepository.find(queryParams, "", null, null);

        assertTrue(patients.size() > 0);
    }

}
