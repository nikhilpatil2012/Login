import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginDetail extends Activity {

	private FragmentManager manager;
	private ActionBar bar;
	private Fragment signUp;
	private boolean isSignUpEnabled;
	private TextView actionBarText;
	private Button back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_options);
		
		bar = getActionBar();
		
		initActionBar();
		
		signUp = new SignUp();
		
		((Button) findViewById(R.id.button3))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						manager = (FragmentManager) getFragmentManager();
						FragmentTransaction trans = manager.beginTransaction();
						trans.replace(android.R.id.content, signUp);
						trans.commit();
						isSignUpEnabled = true;
						back.setVisibility(View.VISIBLE);
						actionBarText.setText("SignUp");
					}
				});
	}

	private void initActionBar()
	{
		bar.setTitle("Login");
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayShowTitleEnabled(false);
		
		LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mCustomView.setLayoutParams(params); 
		bar.setCustomView(mCustomView);
		
		actionBarText = (TextView)mCustomView.findViewById(R.id.nameOfScreen);
		
		actionBarText.setText("Login");
		
		back = (Button)mCustomView.findViewById(R.id.back);//
		back.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("DEMO", "clicked");
				removeCurrentFragment();
	    		actionBarText.setText("Login");
				isSignUpEnabled = false;
				back.setVisibility(View.INVISIBLE);
			}
		});
		bar.setDisplayShowCustomEnabled(true);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(isSignUpEnabled){
	    		removeCurrentFragment();
	    		back.setVisibility(View.INVISIBLE);
	    		actionBarText.setText("Login");
				isSignUpEnabled = false;
	    		return true;
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void removeCurrentFragment()
	{
		if(signUp != null)
		{
			FragmentTransaction trans = manager.beginTransaction();
			trans.remove(signUp);
			trans.commit();
		}
	}
}
