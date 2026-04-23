package chat.client.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FriendsListModel extends AbstractListModel {
    private List<String> friends;

    public FriendsListModel() {
        friends=new ArrayList<String>();
    }

    public int getSize() {
        return friends.size();
    }

    public Object getElementAt(int index) {
        return friends.get(index);
    }

    public void friendLoggedIn(String id){
        friends.add(id);
        fireContentsChanged(this,friends.size()-1,friends.size());
    }

    public void friendLoggedOut(String id){
        friends.remove(id);
        fireContentsChanged(this,0, friends.size());
    }
}
