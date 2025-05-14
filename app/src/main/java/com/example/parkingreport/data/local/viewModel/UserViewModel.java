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
    // back thread
    public void postUser(User user) {
        userLive.postValue(user);
    }
    public LiveData<User> getUserLive() { return userLive; }

    public User getUser() { return userLive.getValue(); }

    /**
     * Constructor
     * @param application
     */
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application.getApplicationContext());
        allUserLive = userRepository.getAllUserLive();
    }

    /**
     * Clears all user data.
     */
    public void clearUser(){
        executeAsync(() -> {
            userRepository.clearUser();
        });
    }

    /**
     * Inserts a new user;
     *
     * @param user the User object to insert
     */
    public void insertUser(User user) {
        executeAsync(() -> {
            userRepository.insertUser(user);
        });
    }

    /**
     * Finds the user ID for the given username.
     *
     * @param name  the username to look up
     * @return int  the matching user ID, or -1 if no such user exists
     */
    public int findIdByName(String name) {
        return userRepository.findIdByName(name);
    }

    /**
     * Changes the password of the user with the given ID and logs the password change.
     *
     * @param ID  the unique identifier of the user whose password is to be changed
     * @param pwd  the new password to set
     * @return Boolean  true if the user exists and the password was successfully changed; false otherwise
     */
    public boolean changeUserPassword(int ID, String pwd){
        return userRepository.changeUserPassword(ID, pwd);
    }

    /**
     * Finds and returns a User by its unique ID.
     *
     * @param userId  the unique identifier of the user to find
     * @return User  the matching User object, or null if not found
     */
    public User findUser(int userId) {
        User user = userRepository.findUser(userId);
        return user;
    }

    /**
     * Executes the given task asynchronously on a background thread,
     * and posts any exception to the main thread.
     *
     * @param task the Runnable task to execute asynchronously
     */
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

    /**
     * Asynchronously checks if a user exists with the given username or email, and returns the result or error via callback.
     *
     * Executes the existence check on a background thread by calling userRepository.checkUserExists(username, email).
     * Posts callback.onResult(exists) to the main thread via mainHandler with the boolean result.
     * If an exception occurs during execution, posts callback.onError(e) to the main thread.
     *
     * @author u8016457 Nanxuan Xie
     * @param username  the username to check for existence
     * @param email  the email address to check for existence
     * @param callback  the callback interface to receive the existence result or error
     */
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

    /**
     * Validates the provided username (or email), password, and role against the user list and returns the result via callback.
     *
     * Registers an observeForever listener on userRepository.getAllUserLive().
     * When the list updates, iterates through each User:
     *     If name/email, password, and role all match, calls resultCallback.accept(true), removes the observer, and returns.
     * If no match is found after iteration, calls resultCallback.accept(false) and removes the observer.
     *
     * @param username  the username or email to validate
     * @param password  the password to validate
     * @param role  the user role to validate
     * @param resultCallback  callback receiving the validation result; true if validation succeeds, false otherwise
     */
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
