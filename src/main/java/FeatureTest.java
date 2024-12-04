import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureTest {
    public static void main(String[] args) {
        log.info("Hello, World!");
        log.info("More");
        String string = null;
        log.info(string.lenght());
    }

    public int square(int variable){
        return variable * variable;
    }
}
