package ceui.lisa.models;

public class ErrorResponse {


    /**
     * has_error : true
     * errors : {"system":{"message":"103:pixiv ID、またはメールアドレス、パスワードが正しいかチェックしてください。","code":1508}}
     */

    private boolean has_error;
    private ErrorsBean errors;
    private ErrorBean error;
    private ErrorBodyBean body;

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public boolean isHas_error() {
        return has_error;
    }

    public void setHas_error(boolean has_error) {
        this.has_error = has_error;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public ErrorBodyBean getBody() {
        return body;
    }

    public void setBody(ErrorBodyBean body) {
        this.body = body;
    }

    public static class ErrorsBean {
        /**
         * system : {"message":"103:pixiv ID、またはメールアドレス、パスワードが正しいかチェックしてください。","code":1508}
         */

        private SystemBean system;

        public SystemBean getSystem() {
            return system;
        }

        public void setSystem(SystemBean system) {
            this.system = system;
        }

        public static class SystemBean {
            /**
             * message : 103:pixiv ID、またはメールアドレス、パスワードが正しいかチェックしてください。
             * code : 1508
             */

            private String message;
            private int code;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }
        }
    }

    public static class ErrorBean {
        private String user_message;
        private String message;
        private String reason;
        private User_message_details user_message_details;

        public String getUser_message() {
            return user_message;
        }

        public void setUser_message(String user_message) {
            this.user_message = user_message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public static class User_message_details {
            private String profile_image;

            public String getProfile_image() {
                return profile_image;
            }

            public void setProfile_image(String profile_image) {
                this.profile_image = profile_image;
            }
        }

        public User_message_details getUser_message_details() {
            return user_message_details;
        }

        public void setUser_message_details(User_message_details user_message_details) {
            this.user_message_details = user_message_details;
        }
    }
}
