package Core;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class Account {
    private final  long   processId;
    private        String email;
    private        String password;
    private static int    accountId = 0;

    public Account(
        String email,
        String password
    ) {
        this.email    = email;
        this.password = password;
        processId     = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public java.lang.String toString() {
        return "Core.Account{" + "email=" + email + ", password=" + password + ", processId=" +
            processId + ", i=" + accountId + '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account() {
        processId = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        if (accountId == 0) {
            try {
                File targetFile = new File("./target/pids/" + processId);
                File parentDir  = targetFile.getParentFile();
                if (!parentDir.exists() && !parentDir.mkdirs()) {
                    throw new IllegalStateException("Can't create dir: " + parentDir);
                }
                if (!targetFile.createNewFile()) {
                    throw new IllegalStateException("Can't create file: " + processId);
                }
                File[] files = parentDir.listFiles();
                if (files != null) {
                    accountId = files.length;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        email    = Config.getProperty("email_" + accountId);
        password = Config.getProperty("password_" + accountId);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getProcessId() {
        return processId;
    }
}
