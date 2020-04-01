package in.abinash.com.go_captcha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity{

    EditText captchaEdt, userIdEdt, passwordEdt;
    ImageView captchaImg;
    ImageButton fingerprintImg, vibrationImg,shakeImg;

    public boolean value = false;

    Button okBtn;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";


    Button submitBtn, registerBtn;
    Vibrator vibrator;
    ImageView imageView;
    private int tap, flag, randomNumber;
    LinearLayout lin, lin1, lin2, lin3;


    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okBtn = (Button)findViewById(R.id.ok_btn);

        userIdEdt = (EditText)findViewById(R.id.user_id_edt);
        passwordEdt = (EditText)findViewById(R.id.password_edt);


        captchaEdt = (EditText)findViewById(R.id.captcha_edt);
        fingerprintImg = (ImageButton)findViewById(R.id.fingerprint_img);
        vibrationImg = (ImageButton)findViewById(R.id.vibrate_img);
        shakeImg = (ImageButton)findViewById(R.id.shake_img);
        captchaImg = (ImageView) findViewById(R.id.captcha_img);

        imageView = (ImageView)findViewById(R.id.tap_img);
        submitBtn = (Button)findViewById(R.id.submit_btn);
        registerBtn = (Button)findViewById(R.id.register_btn);
        lin = (LinearLayout)findViewById(R.id.lin);
        lin1 = (LinearLayout)findViewById(R.id.lin1);
        lin2 = (LinearLayout)findViewById(R.id.lin2);
        lin3 = (LinearLayout)findViewById(R.id.lin3);




        lin.setVisibility(View.VISIBLE);
        lin1.setVisibility(View.VISIBLE);
        lin2.setVisibility(View.VISIBLE);
        lin3.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String captcha = captchaEdt.getText().toString();
                String user = userIdEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if (!password.isEmpty() || !user.isEmpty()) {

                    if (password.equals("123") && user.equals("admin")) {
                        value = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid user Id or password", Toast.LENGTH_SHORT).show();
                    }

                    if (captcha.equals("PQJRYD") && value) {
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (!captcha.equals("PQJRYD")) {
                        Toast.makeText(getApplicationContext(), "Invalid captcha", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid user Id or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter the login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });




        flag = 0;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        fingerprintImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Place your finger in fingerprint sensor", Toast.LENGTH_SHORT).show();


                String user = userIdEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if ( !password.isEmpty() || !user.isEmpty() ) {

                    if (password.equals("123") && user.equals("admin")){
                        fDetect();
                        value = true;
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid user id or password", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Enter the login credentials", Toast.LENGTH_SHORT).show();
                }


            }
        });

        vibrationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Count the vibration and tap", Toast.LENGTH_SHORT).show();

                String user = userIdEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if ( !password.isEmpty() || !user.isEmpty() ) {

                    if (password.equals("123") && user.equals("admin")){
                        vDetect();
                        value = true;
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid user Id or password", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter the login credentials", Toast.LENGTH_SHORT).show();
                }


            }
        });

        shakeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Shake your phone", Toast.LENGTH_SHORT).show();

                flag = 1;
                String user = userIdEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if ( !password.isEmpty() || !user.isEmpty() ) {

                    if (password.equals("123") && user.equals("admin")){
                        sDetect();
                        value = true;
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid user ID or password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Enter the login credentials", Toast.LENGTH_SHORT).show();
                }

                /*mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

                Objects.requireNonNull(mSensorManager).registerListener(mSensorListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
                mAccel = 10f;
                mAccelCurrent = SensorManager.GRAVITY_EARTH;
                mAccelLast = SensorManager.GRAVITY_EARTH;*/


//
//                mAccel = 10f;
//                mAccelCurrent = SensorManager.GRAVITY_EARTH;
//                mAccelLast = SensorManager.GRAVITY_EARTH;

            }
        });

    }

    private void vDetect() {

        lin.setVisibility(View.INVISIBLE);
        lin1.setVisibility(View.INVISIBLE);
        lin2.setVisibility(View.INVISIBLE);
        lin3.setVisibility(View.INVISIBLE);
        registerBtn.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);



        Random random = new Random();
        randomNumber = random.nextInt(10 );

        Log.d("count1", randomNumber + "" );


        if (randomNumber == 0 || randomNumber == 1 ){

            randomNumber = 2;

        }

        long[] vibrateTime = {250,300,250,300,250,300,250,300,250,300,250,300,250,300,250,300,250,300};

        int limit = randomNumber*2;

        long[] count = Arrays.copyOfRange(vibrateTime,0,limit);

        Log.d("count", randomNumber + "" );

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createWaveform(count, -1));

        }else
            vibrator.vibrate(vibrateTime, randomNumber);

        tap = 0;



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("tap", tap+"");

                if( tap == randomNumber ){
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent( getApplicationContext(), WelcomeActivity.class );
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                    lin.setVisibility(View.VISIBLE);
                    lin1.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.VISIBLE);
                    lin3.setVisibility(View.VISIBLE);
                    registerBtn.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                }

            }
        });





    }

    private void sDetect() {

        Selenium selenium = new Selenium();
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private void fDetect() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if(!fingerprintManager.isHardwareDetected()){
                Toast.makeText(this, "Fingerprint Scanner not detected in Device", Toast.LENGTH_SHORT).show();
              //  mParaLabel.setText("Fingerprint Scanner not detected in Device");

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission not granted to use Fingerprint Scanner", Toast.LENGTH_SHORT).show();
               // mParaLabel.setText("Permission not granted to use Fingerprint Scanner");

            } else if (!keyguardManager.isKeyguardSecure()){
                Toast.makeText(this, "Add Lock to your Phone in Settings", Toast.LENGTH_SHORT).show();
               // mParaLabel.setText("Add Lock to your Phone in Settings");

            } else if (!fingerprintManager.hasEnrolledFingerprints()){
                Toast.makeText(this, "You should add atleast 1 Fingerprint to use this Feature", Toast.LENGTH_SHORT).show();
              //  mParaLabel.setText("You should add atleast 1 Fingerprint to use this Feature");

            } else {
                Toast.makeText(this, "Place your Finger on Scanner to Access the App.", Toast.LENGTH_SHORT).show();
               // mParaLabel.setText("Place your Finger on Scanner to Access the App.");

                generateKey();

                if (cipherInit()){

                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);

                }
            }

        }
    }
//-----------------------------------------------------------------------
    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }
    //--------------------------------------------------------------
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 15 && flag == 1 && x < 9.8 && y <9.8 && z < 9.8 ) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( getApplicationContext(), WelcomeActivity.class );
                startActivity(intent);
                finish();

                flag=0;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
      //  mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        //        SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
//-----------------------------------------------------
    public void count(View view) {

        if(view.getId() == R.id.tap_img ) {

            tap++;
            Log.d("tap count" , tap + "");
        }

    }

    //--------------------------------------------------------------
}
