package swtp7.greenc.Service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import swtp7.greenc.Model.GreenConfigurator;
import swtp7.greenc.Model.ModelReader;
import swtp7.greenc.Model.SoftwareSystem;
import swtp7.greenc.Model.StandardModelReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = GreenConfigurator.class)
public class GreenConfiguratorConfiguration {

    /**
     * Bean that will read the SoftwareSystems from disk passing the files provided by Mondays For Future to the
     * StandardModelReader.
     *
     * @return List<SoftwareSystem> a list of the SoftwareSystems present on disk
     */
    @Bean
    public List<SoftwareSystem> readSoftwareSystemsFromDisk() {
        List<SoftwareSystem> softwareSystems = new ArrayList<>();

        File hsqldbXml = createTemporaryCopyOfExistingFile("SoftwareSystems/HSQLDB/FeatureModel.xml");
        File hsqldbDimacs = createTemporaryCopyOfExistingFile("SoftwareSystems/HSQLDB/FeatureModel.dimacs");
        File hsqldbCsv = createTemporaryCopyOfExistingFile("SoftwareSystems/HSQLDB/model.csv");

        ModelReader hsqldbModelReader = new StandardModelReader(hsqldbXml, hsqldbDimacs, hsqldbCsv);
        SoftwareSystem hsqldb = new SoftwareSystem("HSQLDB",
                hsqldbModelReader.getBinaryOptions(),
                hsqldbModelReader.getNumericOptions(),
                hsqldbModelReader.getProperties(),
                hsqldbModelReader.getClauses(),
                hsqldbModelReader.getInfluences());
        softwareSystems.add(hsqldb);

        File sevenZipXml = createTemporaryCopyOfExistingFile("SoftwareSystems/7-Zip/FeatureModel.xml");
        File sevenZipDimacs = createTemporaryCopyOfExistingFile("SoftwareSystems/7-Zip/FeatureModel_noNumeric" +
                ".dimacs");
        File sevenZipCsv = createTemporaryCopyOfExistingFile("SoftwareSystems/7-Zip/model2.csv");

        ModelReader sevenZipModelReader = new StandardModelReader(sevenZipXml, sevenZipDimacs, sevenZipCsv);
        SoftwareSystem sevenZip = new SoftwareSystem("7-Zip", sevenZipModelReader.getBinaryOptions(),
                sevenZipModelReader.getNumericOptions(), sevenZipModelReader.getProperties(),
                sevenZipModelReader.getClauses(), sevenZipModelReader.getInfluences());
        softwareSystems.add(sevenZip);

        return softwareSystems;
    }

    /**
     * Helper method to create a temporary copy of an existing file. This is needed because the original files are
     * will be packed in the jar where they can't be accessed as usual.
     *
     * @param path the path to the file to be copied
     * @return the copy of the file
     */
    private File createTemporaryCopyOfExistingFile(String path) {
        File file = null;
        try {
            InputStream inputStream = new ClassPathResource(path).getInputStream();

            String tempFileName = path.split("/")[path.split("/").length - 1];
            file = File.createTempFile(tempFileName, ".tmp");
            file.deleteOnExit();

            java.nio.file.Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            IOUtils.closeQuietly(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Adds CORS settings that are needed for communicating with the frontend on a different url.
     *
     * @return the CORS filter bean
     */
    @Bean
    public FilterRegistrationBean simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        List<String> exposedHeaders = new ArrayList<>();
        exposedHeaders.add("Access-Control-Allow-Origin");
        exposedHeaders.add("Access-Control-Allow-Credentials");
        config.setExposedHeaders(exposedHeaders);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
