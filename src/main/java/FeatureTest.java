import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureTest {
    public static void main(String[] args) {
        log.info("Hello, World!");
        log.info("More");
    }

    public int square(int variable){
        return variable * variable;
    }
}
