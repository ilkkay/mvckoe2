package translateit2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Locale;

/**
 *
 * Used widely in this project for localizing messages and in unit testing for
 * asserting that the exception returned was a correct one.
 * 
 * @author Ilkka
 *
 */
@Component
public class Messages {

    private MessageSource messageSource;

    @Autowired
    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() { // default is finnish for gui
        accessor = new MessageSourceAccessor(messageSource, new Locale("fi"));
    }

    // tests will be using english
    public void init(Locale locale) {
        accessor = new MessageSourceAccessor(messageSource, locale);
    }

    public String get(String code, String[] args) {
        String msg = null;
        try {
            msg = accessor.getMessage(code, args);
        } catch (NoSuchMessageException e) {
            return "Text not implemented for " + code;
        }

        return msg;
    }

    public String get(String code) {
        String msg = null;
        try {
            msg = accessor.getMessage(code);
        } catch (NoSuchMessageException e) {
            return "Text not implemented for " + code;
        }

        return msg;
    }

    // Used in unit testing to check that the exception message is the one
    // we are expecting. The code returns only part of the message because
    // of message parameters we cannot compare the whole message
    //
    // Segment size must be between {0} and {1} characters
    //
    public String getPart(String code) {
        String msg = null;
        try {
            msg = accessor.getMessage(code);
        } catch (NoSuchMessageException e) {
            return "Text not implemented for " + code;
        }

        String parts[] = msg.split("[{]");
        if (parts.length == 1)
            return msg;

        int maxLen = -1;
        String maxStr = "";
        for (String s : parts) {
            if (s.length() > maxLen) {
                maxStr = s;
                maxLen = s.length();
            }
        }

        parts = maxStr.split("[}]");
        if (parts.length == 1)
            return maxStr;
        else
            return parts[1];
    }

}
