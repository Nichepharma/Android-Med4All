package p.com.med4all.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import APIS.ServerRequest;
import Libs.StaticVars;
import Libs._SharedPref;
import p.com.med4all.R;

public class PhoneNumerAuth extends AppCompatActivity {
    private static final String TAG = PhoneNumerAuth.class.getSimpleName();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText mVerificationField;
    private TextView lbl_currentPhoneNumber ;
    private Button  mVerifyButton, mResendButton;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    JSONObject userCompliteData = new JSONObject();
    String phoneNumber = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numer_auth);


        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);

        lbl_currentPhoneNumber = findViewById(R.id.lbl_currentPhoneNumber);
        mAuth = FirebaseAuth.getInstance();




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationField.setError("برجاء ادخال رمز التفعيل صحيحا");

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;
            }
        };


        Bundle bundle = getIntent().getExtras();
        try {
            userCompliteData = new JSONObject( bundle.getString("userData") );
            phoneNumber = "+2"+userCompliteData.getString(getResources().getString(R.string.user_key_phone));
            lbl_currentPhoneNumber.setText(phoneNumber);
            startPhoneNumberVerification(phoneNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public  void  startVerify(View view){
        String code = mVerificationField.getText().toString();
        if (TextUtils.isEmpty(code)) {
            mVerificationField.setError("ادخل رمز التفعيل");
            return;
        }
        verifyPhoneNumberWithCode(mVerificationId, code);
    }

        public void resendVerificationCodeAgain(View view) {
                resendVerificationCode(phoneNumber, mResendToken);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        try {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);

        }catch (Exception ex) {
            mVerificationField.setError("برجاء ادخال رمز التفعيل صحيحا");

        }

    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private boolean validatePhoneNumber() {

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(StaticVars.getContext(), "Invalid phone number." , Toast.LENGTH_LONG ).show();
            return false;
        }
        return true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("include", "userModel");
                            params.put("ask", "CREATE");
                            params.put("OS", "ANDROID");
                            params.put("data", userCompliteData.toString());

                            new ServerRequest.Connection_(params){
                                @Override
                                public void didStart() {
                                    super.didStart();
                                }

                                @Override
                                public void didFinsh(String dataRequest) {
                                    super.didFinsh(dataRequest);
                                    if (!dataRequest.equals("0")) {
                                        String userID = dataRequest ;
                                        try
                                        {
                                            userCompliteData.put(getResources().getString(R.string.user_key_id), userID);
                                            _SharedPref.setByArray(getApplicationContext(), userCompliteData);
                                            Toast.makeText(StaticVars.getContext() ,  "تم تسجيل بنجاح" ,Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(StaticVars.getContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }else {
                                        Toast.makeText(StaticVars.getContext() ,  "حدث خطاء ما , لم يتم تسجيل بنجاح" ,Toast.LENGTH_LONG).show();

                                    }

                                }
                            };



                                // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
