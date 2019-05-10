package fr.pchab.androidrtc.model;

public class LoginState {
    private boolean isMaster;
    private boolean valid;

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
