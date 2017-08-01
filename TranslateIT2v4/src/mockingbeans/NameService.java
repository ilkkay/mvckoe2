package translateit2.mockingbeans;

import org.springframework.stereotype.Service;

//just for testing
@Service
public class NameService {
    public String getUserName(String id) {
        return "Real user name";
    }
}
