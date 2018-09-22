package android.com.avishkar;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private Button btnSignin;
    private Button skip,skipL;
    private FirebaseAuth firebaseAuth;
    String red_em;
    GoogleSignInClient mGoogleSignInClient;
    private TextInputEditText memail,mpassword,signup_memail,signup_mpass,signup_mob;
    LoginButton facebook;
    Button google;
    View login_view,signin_view;
    String g_email,f_email;
    private static int RC_SIGN_IN=1;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("418717225322021");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("296397114320-jfk9dnk77n4n98tda8gkr338fj31f7i6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (googleSignInAccount!=null)
        {
            g_email=googleSignInAccount.getEmail();
            red_em=parse(g_email);
            UserLogin user=new UserLogin(g_email,"N/A",0,0,0);
            FirebaseDatabase mfirebase=FirebaseDatabase.getInstance();
            DatabaseReference myref = mfirebase.getReference();
            myref.child("users").child(red_em).child("profile").setValue(user);
        }
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        facebook=(LoginButton)findViewById(R.id.fblogin);
        callbackManager = CallbackManager.Factory.create();
        facebook.setReadPermissions("email","public_profile");
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(Main2Activity.this, "facebook logged in", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Main2Activity.this,"no",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Main2Activity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        tvSigninInvoker=(TextView) findViewById(R.id.tvSigninInvoker);
        tvSignupInvoker=(TextView) findViewById(R.id.tvSignupInvoker);
        login_view = (View)findViewById(R.id.llSigninContent);
        signin_view = (View)findViewById(R.id.llSignupContent);
        btnSignin=(Button) findViewById(R.id.btnSignin);
        btnSignup=(Button) findViewById(R.id.btnSignup);
        signup_memail = (TextInputEditText)signin_view.findViewById(R.id.signup_email);
        signup_mpass = (TextInputEditText)signin_view.findViewById(R.id.signup_password);
        signup_mob = (TextInputEditText) signin_view.findViewById(R.id.mob);
        memail=(TextInputEditText)login_view.findViewById(R.id.email);
        mpassword=(TextInputEditText)login_view.findViewById(R.id.password);
        skip=(Button)findViewById(R.id.skip);
        skipL=(Button)findViewById(R.id.skipL);
        skipL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte=new Intent(Main2Activity.this,Home.class);
                inte.putExtra("id",0);
                startActivity(inte);
                finish();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte=new Intent(Main2Activity.this,Home.class);
                inte.putExtra("id",0);
                startActivity(inte);
                finish();
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(memail.getText().toString().trim(),mpassword.getText().toString().trim()).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())
                        Toast.makeText(Main2Activity.this, "error logging in"+task.getException(), Toast.LENGTH_LONG).show();
                    else
                    {
                        Intent temp=new Intent(Main2Activity.this,Dashboard.class);
                        red_em=parse(memail.getText().toString());
                        temp.putExtra("email",red_em);
                        temp.putExtra("id",1);
                        startActivity(temp);
                    }
                }
            });
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(signup_memail.getText().toString().trim()))
                {
                    Toast.makeText(Main2Activity.this,"Enter email address",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (TextUtils.isEmpty(signup_mpass.getText().toString().trim()))
                {
                    Toast.makeText(Main2Activity.this,"Enter email password",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (signup_mpass.getText().toString().trim().length()<6)
                {
                    Toast.makeText(Main2Activity.this,"Password too short",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (TextUtils.isEmpty(signup_mob.getText().toString().trim()))
                {
                    Toast.makeText(Main2Activity.this,"Enter mobile number",Toast.LENGTH_LONG).show();
                    return ;
                }
                firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(signup_memail.getText().toString().trim(),signup_mpass.getText().toString()).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Snackbar.make(getCurrentFocus(), "Authentication failed", Snackbar.LENGTH_LONG).show();
                        }
                        else
                        {
                            Snackbar.make(getCurrentFocus(), "User Registered", Snackbar.LENGTH_SHORT).show();

                            red_em=parse(signup_memail.getText().toString());
                            UserLogin user = new UserLogin(signup_memail.getText().toString(),signup_mob.getText().toString(),0,0,0);
                            FirebaseDatabase mfirebase=FirebaseDatabase.getInstance();
                            DatabaseReference myref = mfirebase.getReference();
                            myref.child("users").child(red_em).child("profile").setValue(user);
                        }
                    }
                });

            }
        });
        llSignin=(LinearLayout) findViewById(R.id.llSignin);
        llSignup=(LinearLayout) findViewById(R.id.llSignup);
        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSigninForm();
            }
        });
        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignupForm();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("failed", "Google sign in failed", e);
            }
        }
        else
        {
            Log.e("fb",resultCode+"");
            if (resultCode==0)
            {
                Snackbar.make(getCurrentFocus(),"Try Again Later",Snackbar.LENGTH_LONG).show();
            }
            else {
                UserLogin user = new UserLogin(f_email,"N/A",0,0,0);
                FirebaseDatabase mfirebase=FirebaseDatabase.getInstance();
                DatabaseReference myref = mfirebase.getReference().child("users").child(red_em).child("profile").child("email");
                myref.setValue(f_email);
                Intent intent= new Intent(Main2Activity.this,MapsActivity.class);
                intent.putExtra("id",1);
                intent.putExtra("email",red_em);
                startActivity(intent);
                finish();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("mailed", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("success", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Snackbar.make(getCurrentFocus(),"done",Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(Main2Activity.this,MapsActivity.class);
                            intent.putExtra("email",red_em);
                            intent.putExtra("id",1);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("failed", "signInWithCredential:failure", task.getException());
                            Snackbar.make(getCurrentFocus(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
      private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.  widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }
    public String parse(String email)
    {
        String red_email="";
        for (int i = 0 ;i<email.length();i++)
        {
            if ((email.charAt(i)>='A'&&email.charAt(i)<='Z')||(email.charAt(i)>='a'&&email.charAt(i)<='z')||(email.charAt(i)>='0'&&email.charAt(i)<='9'))
                red_email+=email.charAt(i);
        }
        return red_email;
    }
}