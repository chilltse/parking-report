package com.example.parkingreport.data.local.viewModel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.parkingreport.data.local.api.Callback;
import com.example.parkingreport.data.local.entities.User;
import com.example.parkingreport.data.local.repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    LiveData<List<User>> allUserLive;

    // 线程管理工具
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final MutableLiveData<Integer> userIdLive = new MutableLiveData<>();
    public void setUserId(int id) { userIdLive.setValue(id); }
    public LiveData<Integer> getUserIdLive() { return userIdLive; }
    public int getUserId() { return userIdLive.getValue() != null ? userIdLive.getValue() : -1; }


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        allUserLive = userRepository.getAllUserLive();
    }

//    public LiveData<List<User>> getAllUserLive() {
//        return allUserLive;
//    }

    public void clearUser(){
        executeAsync(() -> {
            userRepository.clearUser();
        });
    }

    public void insertUser(User user) {
        executeAsync(() -> {
            userRepository.insertUser(user);
        });
    }
    public void deleteUser(User user) {
        executeAsync(() -> {
            userRepository.deleteUser(user);
        });
    }

    public int findIdByName(String name)
    {
        return userRepository.findIdByName(name);
    }

    public boolean changeUserPassword(int ID, String pwd){
        return userRepository.changeUserPassword(ID, pwd);
    }

//    public User findUser(int userId, Callback<User> callback) {
    public User findUser(int userId) {
//        executeAsync(() -> {
            User user = userRepository.findUser(userId);
//            callback.onResult(user);
//        });
        return user;
    }



//    public void modifyUserName(int userId, String name) {
//        executeAsync(() -> {
//            userRepository.modifyUserName(userId, name);
//        });
//    }

    public void modifyUserPassword(int userId, String password) {
        executeAsync(() -> {
            userRepository.modifyUserPassword(userId, password);
        });
    }
    // 通用异步执行方法
    private void executeAsync(Runnable task) {
        executor.execute(() -> {
            try {
                task.run();
            } catch (Exception e) {
                mainHandler.post(() -> new Exception("Exception: " + e)
                );
            }
        });
    }

    public interface UserCheckCallback {
        void onResult(boolean exists);
        void onError(Exception e);
    }

    public void isUserOrEmailExists(String username, String email, UserCheckCallback callback) {
        executeAsync(() -> {
            try {
                boolean exists = userRepository.checkUserExists(username, email);
                mainHandler.post(() -> callback.onResult(exists));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
public void validateUser(String username, String password, int role, Consumer<Boolean> resultCallback) {
    userRepository.getAllUserLive().observeForever(new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> users) {
            for (User user : users) {
                if ((username.equals(user.getName()) || username.equals(user.getEmail()))
//                        && BCrypt.checkpw(password, user.getPassword())
                        && password.equals(user.getPassword())
                        && role == user.getRole()) {
                    resultCallback.accept(true);
                    userRepository.getAllUserLive().removeObserver(this);
                    return;
                }
            }
            resultCallback.accept(false);
            userRepository.getAllUserLive().removeObserver(this);
        }
    });
}



}
