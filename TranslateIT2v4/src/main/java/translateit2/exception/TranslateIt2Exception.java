package translateit2.exception;

import java.io.IOException;

public class TranslateIt2Exception extends IOException  /* RuntimeException */  {
    private static final long serialVersionUID = 1L;

    private TranslateIt2Error errorCode;

    public TranslateIt2Error getErrorCode(){
        return this.errorCode;
    }

    public TranslateIt2Exception(TranslateIt2Error errorCode) {
        this.errorCode = errorCode;
    }
    
    public TranslateIt2Exception(TranslateIt2Error errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }

    public TranslateIt2Exception(TranslateIt2Error errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    //
    //  *********************************************    
    //

    public TranslateIt2Exception(String message) {
        super(message);
    }

    public TranslateIt2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public TranslateIt2Exception(Throwable cause) {
        super(cause);
    }

}
