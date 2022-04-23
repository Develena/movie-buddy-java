package moviebuddy.data;

import moviebuddy.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class AbstractFileSystemMovieReader {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String metadata;

    public String getMetadata() {
        return metadata;
    }

    @Value("${movie.metadata}")
    public void setMetadata(String metadata) {
        // 반드시 필요한 metadata.
        this.metadata = Objects.requireNonNull(metadata, "metadata is required value.");
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        URL metadataURL = ClassLoader.getSystemResource(metadata);
        if (Objects.isNull(metadataURL)) { // 잘못된 URL일 경우 null
            throw new FileNotFoundException(metadata);
        }

        if (Files.isReadable(Path.of(metadataURL.toURI())) == false) {
            throw new ApplicationException(String.format("cannot read to metadata. [%s]", metadata));
        }

    }

    @PreDestroy
    public void destroy() throws Exception {
        log.info("Destroyed Bean.");
    }
}
