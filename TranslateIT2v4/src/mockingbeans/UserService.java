package translateit2.mockingbeans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//just for testing

@Service
public class UserService {

    private NameService nameService;

    @Autowired
    public UserService(NameService nameService) {
        this.nameService = nameService;
    }

    public String getUserName(String id) {
        return nameService.getUserName(id);
    }
}