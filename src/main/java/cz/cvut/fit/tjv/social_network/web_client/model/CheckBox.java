package cz.cvut.fit.tjv.social_network.web_client.model;

public class CheckBox {
    private boolean check =false;

    public CheckBox() {
    }

    public CheckBox(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
