package VTTPday15.homework.models;

import jakarta.validation.constraints.Email;

public class User {
    
    @Email (message="Must be a valid email")
    private String userId;

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

}
