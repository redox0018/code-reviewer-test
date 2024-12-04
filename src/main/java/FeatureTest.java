import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureTest {
    public static void main(String[] args) {
        log.info("Hello, World!");
    }

    public int square(int variable){
        String string = null;
        log.info(String.valueOf(string.length()));
        return variable * variable;
    }
}
