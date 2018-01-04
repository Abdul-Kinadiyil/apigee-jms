package main.java.receiver.apibeans;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonPropertyOrder({ "StatusCode","message"})
public class APIResponse {
        @JsonProperty("message")
        private String message;
        @JsonProperty("message")
        public String getMessage() {
                return message;
        }
        @JsonProperty("message")
        public void setMessage(String message) {
                this.message = message;
        }

        @JsonProperty("StatusCode")
        private String statuscode;
        @JsonProperty("StatusCode")
		public String getStatuscode() {
			return statuscode;
		}
        @JsonProperty("StatusCode")
		public void setStatuscode(String statuscode) {
			this.statuscode = statuscode;
		}


}
