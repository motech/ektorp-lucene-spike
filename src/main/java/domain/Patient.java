package domain;


import lombok.Data;
import org.ektorp.support.CouchDbDocument;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Data
public class Patient extends CouchDbDocument {

    private String patientId;
    private String name;
    private String phoneNumber;
    private LocalDate dob;
    private boolean onActiveTreatment = true;
    private Integer age;

    private List<Address> addresses = new ArrayList<Address>();

    public Patient withAddresses(Address... arrAddress) {
        for(Address address : arrAddress)
            addresses.add(address);
        return this;
    }
}
