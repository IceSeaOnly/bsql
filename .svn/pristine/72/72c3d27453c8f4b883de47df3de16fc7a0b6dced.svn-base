package binghai.Core;

/**
 * Created by Administrator on 2016/12/10.
 * 用户权限
 */
public class UserPrivileges {
    private boolean createDB;
    private boolean shutdown;
    private boolean createUser;
    private boolean grant;

    public UserPrivileges(boolean createDB, boolean shutdown, boolean createUser, boolean grant) {
        this.createDB = createDB;
        this.shutdown = shutdown;
        this.createUser = createUser;
        this.grant = grant;
    }

    public UserPrivileges() {
        this.createDB = true;
        this.shutdown = true;
        this.createUser = true;
        this.grant = true;
    }

    public boolean isCreateDB() {
        return createDB;
    }

    public void setCreateDB(boolean createDB) {
        this.createDB = createDB;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public boolean isCreateUser() {
        return createUser;
    }

    public void setCreateUser(boolean createUser) {
        this.createUser = createUser;
    }

    public boolean isGrant() {
        return grant;
    }

    public void setGrant(boolean grant) {
        this.grant = grant;
    }
}
