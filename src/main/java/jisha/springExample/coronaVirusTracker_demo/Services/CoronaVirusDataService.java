package jisha.springExample.coronaVirusTracker_demo.Services;


import jisha.springExample.coronaVirusTracker_demo.Model.LocationStats;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class CoronaVirusDataService {
    @Setter
    public static String DATA_SOURCE="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @Autowired
    private List<LocationStats> allStats;
    @PostConstruct
    @Scheduled(cron = "* * * * * *")
    public void fetchVirusData() throws IOException, InterruptedException {

        HttpClient client=HttpClient.newHttpClient();
        HttpRequest  request=HttpRequest.newBuilder().uri(URI.create(DATA_SOURCE)).build();

        HttpResponse<String> httpResponse=client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());

        StringReader csvBodyReader=new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            String state = record.get("Province/State");
            String Country = record.get("Country/Region");
            System.out.println(state+"   "+Country);

//            String name = record.get("Name");
        }

    }
}
