package com.example.parkingreport.data.local.viewModel;

/**
 * @author @u7864325 Weimiao Sun
 * ViewModel class for managing User data and operations.
 *
 * Responsibilities:
 * - Interfaces with the {UserRepository} to perform user-related data actions
 * - Supports asynchronous insertion, deletion, password modification, and existence checks
 * - Exposes selected user info via {LiveData} for UI binding
 * - Provides identity verification and duplicate checking for login or registration flows
 *
 * This ViewModel ensures thread-safe, lifecycle-aware operations to prevent memory leaks
 * and UI blocking during user management processes.
 */

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    // Thread Management Method
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // User
    private final MutableLiveData<User> userLive = new MutableLiveData<>();
    // Main thread
    public void setUser(User user) {
        userLive.setValue(user);
    }
    // Main thread
    public void postUser(User user) {
        userLive.postValue(user);
    }
    public LiveData<User> getUserLive() { return userLive; }
    public User getUser() { return userLive.getValue(); }


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application.getApplicationContext());
        allUserLive = userRepository.getAllUserLive();
    }


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


    public void modifyUserPassword(int userId, String password) {
        executeAsync(() -> {
            userRepository.modifyUserPassword(userId, password);
        });
    }

    // General synchronous execution method
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
public void validateUser(String username, String password, String role, Consumer<Boolean> resultCallback) {
    userRepository.getAllUserLive().observeForever(new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> users) {
            for (User user : users) {
                Log.d("verify password, username:", "user.getRole()"+user.getRole());
                Log.d("verify password, username:", "role"+role);
                if ((username.equals(user.getName()) || username.equals(user.getEmail()))
//                        && BCrypt.checkpw(password, user.getPassword())
                        && password.equals(user.getPassword())
                        && role.equals(user.getRole())) {
                    resultCallback.accept(true);
                    userRepository.getAllUserLive().removeObserver(this);
                    Log.d("verify password, username:", "succeed"+user.getName());
                    return;
                }
            }
            resultCallback.accept(false);
            userRepository.getAllUserLive().removeObserver(this);
        }
    });
}

}
