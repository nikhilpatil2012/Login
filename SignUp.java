import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class SignUp extends Fragment {

	private EditText name, mobile;
	private DatePicker dobPicker;
	private RadioGroup gender;
	private Button submit;
	private String genderDetails, mobileNo;
	private View v;
	private ImageView profileImage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.signupfrag, null);
		
		DisplayMetrics MAT = new DisplayMetrics();

		((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(MAT);

		android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(
				(MAT.heightPixels * 15) / 100, (MAT.heightPixels * 15) / 100);

		profileImage = (ImageView) v.findViewById(R.id.profilePic);
		
		
		profileImage.setLayoutParams(params);
		
		init();
		
		profileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK,	android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				final int ACTIVITY_SELECT_IMAGE = 1235;
				startActivityForResult(i, ACTIVITY_SELECT_IMAGE);	
			}
		});
		
		return v;
	}
	
	private void init() {
		
		name = (EditText) v.findViewById(R.id.name);

		mobile = (EditText) v.findViewById(R.id.mobile);

		dobPicker = (DatePicker) v.findViewById(R.id.datePicker1);

		gender = (RadioGroup) v.findViewById(R.id.gender);

		submit = (Button) v.findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.w("DEMO", "Name :: " + name.getText().toString());
				mobileNo = mobile.getText().toString();
				Log.w("DEMO", "Mobile :: " + mobile.getText().toString());
				Log.w("DEMO",
						"DOB :: " + dobPicker.getDayOfMonth()
								+ dobPicker.getMonth() + dobPicker.getYear());
				if (gender.getCheckedRadioButtonId() == R.id.male) {
					genderDetails = "Male";
					Log.w("DEMO", "Gender :: Male");
				} else {
					genderDetails = "Female";
					Log.w("DEMO", "Gender :: Female");
				}

				String appid = getActivity().getSharedPreferences(
						"USER_DETAILS", Context.MODE_PRIVATE).getString(
						"APP_ID", null);
				if (appid.equals(null)) {
					appid = "shabxhgasxhgavxhavsxsag";
				}

				/*new NetworkLogin(getActivity()).execute(name.getText()
						.toString(), "", "", "" + dobPicker.getDayOfMonth(), ""
						+ (1 + dobPicker.getMonth()), "" + dobPicker.getYear(),
						genderDetails, mobileNo, "india", "delhi", "",
						"No mercy", appid, "FB", "", "");*/

			}
		});
	}
	
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		case 1235:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getActivity().getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				final String filePath = cursor.getString(columnIndex);
				cursor.close();
				
				profileImage.getHandler().post(new Runnable() {
					
					@Override
					public void run() {
						try
						{
							Log.w("DEMO", "path >> "+filePath);
							
							ExifInterface exif = new ExifInterface(filePath);
							int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
							
							
							Log.w("DEMO", "ORIENTATION :: "+orientation);
							
							Bitmap image = BitmapFactory.decodeFile(filePath);
							
							if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
							{
								Matrix mat = new Matrix();
								mat.postRotate(90);
								image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), mat, true);
							}
							if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
							{
								Matrix mat = new Matrix();
								mat.postRotate(180);
								image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), mat, true);
							}
							if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
							{
								Matrix mat = new Matrix();
								mat.postRotate(270);
								image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), mat, true);
							}
							
							
							profileImage.setImageBitmap(image);
							
							File f = new File(filePath);
							
							FileOutputStream out = getActivity().openFileOutput(f.getName(), Context.MODE_PRIVATE);
							
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							
							image.compress(CompressFormat.JPEG, 100, bos);
							
							out.write(bos.toByteArray(), 0, bos.toByteArray().length);
							out.close();
							
							getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE).edit().putString("profile_pic", f.getName()).commit();
							
						}catch(OutOfMemoryError e)
						{
							Log.e("DEMO","opeartion cancelled");
						}catch(Exception e)
						{
							Log.e("DEMO","opeartion cancelled");
						}
						
					}
				});
			}
		}	
	}
	

}
