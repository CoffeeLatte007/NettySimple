package messageEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by lz on 2016/8/11.
 */
public class UserInfo implements Serializable {
    /**
     * 默认序列号
     */
    private static final long serialVersionUID = 1L;
    private String userName;
    private int userID;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID(10);
        userInfo.setUserName("李钊");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = userInfo.getUserName().getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(userInfo.getUserID());
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        System.out.println(result.length);
        System.out.println(b.length);
    }
}
