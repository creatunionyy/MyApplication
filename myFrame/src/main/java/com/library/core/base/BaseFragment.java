package com.library.core.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.library.core.adapter.MenuItemAdapter;
import com.library.core.event.BaseEventManager.OnEventListener;
import com.library.core.event.BaseEventManager.OnEventRunner;
import com.library.core.event.Event;
import com.library.core.event.XEventManager;
import com.library.core.receiver.NetworkStateListener;
import com.library.core.receiver.NetworkStateReceiver;
import com.library.core.utils.NetworkUtil;
import com.library.core.utils.ToastUtil;
import com.library.core.view.ProgressDialog;
import com.library.google.zxing.CaptureActivity;
import com.library.google.zxing.encoding.EncodingHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzc.frame.R;

public abstract class BaseFragment extends Fragment implements
		OnEventListener, OnClickListener, OnItemClickListener {

	public static final String EXTRA_HasTitle = "hastitle";
    private BaseAttribute mBaseAttribute;
    private RelativeLayout mBackButton;
    private TextView mTitle;
    private ImageView mRightImageView;
    private TextView mRightTextView;

    private int mCurrentChooseLaunch;
    protected static final int REQUESET_CODE = 10000;
    protected static final int TAKE_PICTURE = 10001;
    protected static final int SELECT_PICTURE = 10002;
    protected static final int TAKE_VIDEO = 10003;
    protected static final int SELECT_VIDEO = 10004;
    protected static final int TAKE_AUDIO = 10005;
    protected static final int SELECT_AUDIO = 10006;
    protected static final int SCAN_LIFE = 10007;

    protected static final int MENUID_PHOTO_CAMERA = 1;
    protected static final int MENUID_PHOTO_FILE = 2;

    public XEventManager xEventManager = XEventManager.getInstance();
    private NetworkStateListener networkStateListener;

    private View mViewPromptConnection;
    protected Dialog mLoadingDialog;
    protected ImageLoader mImageLoader;
    private SimpleBaseUIFactory mSimpleUIFractory;
    private ProgressDialog mProgressDialog;
    private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View view = null;

		onInitAttribute(mBaseAttribute = new BaseAttribute());
		if (mBaseAttribute.mFragmentLayoutId != 0) {
			view = inflater.inflate(mBaseAttribute.mFragmentLayoutId, null);
			setContentView(view);
		}
		initViews(view);
		setListener();
		mImageLoader = ImageLoader.getInstance();
		initNetworkStateListener();
		return view;
	}

    protected abstract void onInitAttribute(BaseAttribute ba);

	private void setContentView(View view) {
        mSimpleUIFractory = new SimpleBaseUIFactory(getActivity());

        if (!mBaseAttribute.mHasTopBar)
            return;

        mTitle = (TextView) view.findViewById(R.id.title);
        if(getArguments().getBoolean(EXTRA_HasTitle)){
            String title = getArguments().getString("key");
            if(title != null){
                mTitle.setText(title);
            }
        }else{
            mTitle.setVisibility(View.GONE);
        }

        if (mBaseAttribute.mHasRight) {
            if (mBaseAttribute.mRightResId != 0) {
                mRightImageView = (ImageView) view.findViewById(R.id.iv_right_option);
                mRightImageView.setVisibility(View.VISIBLE);
                mRightImageView.setImageResource(mBaseAttribute.mRightResId);
                mRightImageView.setOnClickListener(mOnClickListener);
            } else {
                mRightTextView = (TextView) view.findViewById(R.id.tv_right_option);
                mRightTextView.setVisibility(View.VISIBLE);
                if (mBaseAttribute.mRightText == null) {
                    mRightTextView.setText(mBaseAttribute.mRightTextStringId);
                } else {
                    mRightTextView.setText(mBaseAttribute.mRightText);
                }
                mRightTextView.setOnClickListener(mOnClickListener);
            }
        }
	}

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mBackButton) {
                //finish();
            } else {
                onRightButtonClicked(v);
            }
        }
    };

    protected void onRightButtonClicked(View v) {
    }

    protected class BaseAttribute {

        public boolean mHasTopBar = true;
        public boolean mHasRight = false;

        public int mFragmentLayoutId;
        public String mRightText;
        public int mRightTextStringId;
        public int mRightResId;

    }

    protected void launchActivityForResult(Class<?> c, Bundle b, int requestCode) {
        Intent i = new Intent(getActivity(), c);
        if (b != null) {
            i.putExtras(b);
        }
        startActivityForResult(i, requestCode == 0 ? REQUESET_CODE
                : requestCode);
    }

    protected void launchActivity(Class<?> c, Bundle b, boolean isCloseSelf) {
        Intent i = new Intent(getActivity(), c);
        if (b != null) {
            i.putExtras(b);
        }
        startActivity(i);
        if (isCloseSelf)
            getActivity().finish();
    }

    protected void launchActivity(Class<?> c) {
        launchActivity(c, null, false);
    }

    protected void launchActivity(Class<?> c, Bundle b) {
        launchActivity(c, b, false);
    }

    protected void launchActivity(Class<?> c, boolean isCloseSelf) {
        launchActivity(c, null, isCloseSelf);
    }

    protected void launchActivityForResult(Class<?> c, Bundle b) {
        Intent i = new Intent(getActivity(), c);
        if (b != null) {
            i.putExtras(b);
        }
        startActivityForResult(i, REQUESET_CODE);
    }

    public void launchActivityForResult(Class<?> c) {
        launchActivityForResult(c, null);
    }

    protected void alterText(String content) {
        ToastUtil.show(content);
    }

    protected void alterText(int id) {
        ToastUtil.show(id);
    }

    protected void setListener() {

    }

    protected void initViews(View view) {

    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    protected void launchCameraPicture() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void launchGalleryPicture() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    protected void launchCameraVideo() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent, TAKE_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void launchGalleryVideo() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_VIDEO);
    }

    protected void launchRecord() {
        Intent openCameraIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(openCameraIntent, TAKE_AUDIO);
    }

    protected void launchMusicStore() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_AUDIO);

    }

    protected void launchCameraScan() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, SCAN_LIFE);
    }

    protected Bitmap createScanByCode(String code) throws Exception {
        if (TextUtils.isEmpty(code)) {
            return EncodingHandler.createQRCode(code, 350);
        } else {
            return null;
        }
    }

    protected void choosePicture() {
        choosePicture(null);
    }

    protected void chooseVideo() {
        chooseVideo(null);
    }

    protected void chooseAudio() {
        chooseAudio(null);
    }

    protected void choosePicture(String title) {
        chooseDilog(title, TAKE_PICTURE);
    }

    protected void chooseVideo(String title) {
        chooseDilog(title, TAKE_VIDEO);
    }

    protected void chooseAudio(String title) {
        chooseDilog(title, TAKE_AUDIO, true);
    }

    protected void chooseDilog(String title, int chooseLaunch) {
        chooseDilog(title, chooseLaunch, false);
    }

    protected void chooseDilog(String title, int chooseLaunch, boolean isRecoder) {
        mCurrentChooseLaunch = chooseLaunch;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        MenuItemAdapter adapter = new MenuItemAdapter(getActivity(),
                new ArrayList<MenuItemAdapter.MenuItem>());
        adapter.addItem(new MenuItemAdapter.MenuItem(MENUID_PHOTO_CAMERA,
                getResources().getString(
                        isRecoder ? R.string.choose_record
                                : R.string.choose_from_albums)));
        adapter.addItem(new MenuItemAdapter.MenuItem(MENUID_PHOTO_FILE,
                getResources().getString(
                        isRecoder ? R.string.choose_yinpin
                                : R.string.choose_from_albums)));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    switch (mCurrentChooseLaunch) {
                        case TAKE_PICTURE:
                            launchCameraPicture();
                            break;
                        case TAKE_VIDEO:
                            launchCameraVideo();
                            break;
                        case TAKE_AUDIO:
                            launchRecord();
                            break;
                    }
                } else if (which == 1) {
                    switch (mCurrentChooseLaunch) {
                        case TAKE_PICTURE:
                            launchGalleryPicture();
                            break;
                        case TAKE_VIDEO:
                            launchGalleryVideo();
                            break;
                        case TAKE_AUDIO:
                            launchMusicStore();
                            break;
                    }
                }
            }
        });
        if (title != null) {
            builder.setTitle(title);
        }
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
                onCameraPictureResult(data);
            } else if (requestCode == SELECT_PICTURE) {
                onPictureChooseResult(data);
            } else if (requestCode == TAKE_VIDEO) {
                onCameraVideoResult(data);
            } else if (requestCode == SELECT_VIDEO) {
                onVideoChooseResult(data);
            } else if (requestCode == TAKE_AUDIO) {
                onRecordResult(data);
            } else if (requestCode == SELECT_AUDIO) {
                onMusicStoreResult(data);
            } else if (requestCode == SCAN_LIFE) {
                onCameraScanResult(data);
            }
        }
    }

    private void onCameraScanResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            onScanResult(bundle.getString("result"));
        }
    }

    private void onCameraPictureResult(Intent data) {
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        Uri uriImageData;
        if (data.getData() != null) {
            uriImageData = data.getData();
        } else {
            uriImageData = Uri.parse(MediaStore.Images.Media.insertImage(
                    mContext.getContentResolver(), bm, null, null));
        }
        Cursor cursor = mContext.getContentResolver().query(uriImageData, null, null,
                null, null);
        if (cursor.moveToFirst()) {
            String filePath = cursor.getString(cursor.getColumnIndex("_data"));
            if (filePath != null && filePath.length() > 0)
                onPictureChoosed(filePath);
        }
        cursor.close();
    }

    private void onPictureChooseResult(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        if (filePath != null && filePath.length() > 0)
            onPictureChoosed(filePath);
    }

    private void onCameraVideoResult(Intent data) {
        if (data == null) {
            @SuppressWarnings("deprecation")
            Cursor cursor = getActivity().managedQuery(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{
                            MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.DURATION}, null, null,
                    MediaStore.Video.VideoColumns.DATE_ADDED + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                final String videoPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Video.Media.DATA));
                final long duration = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Video.Media.DURATION));
                onVideoChoosed(videoPath, duration);
                cursor.close();
            }
        } else {
            Uri videoUri = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = getActivity().managedQuery(videoUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final String videoPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Video.Media.DATA));
                final long duration = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Video.Media.DURATION));
                onVideoChoosed(videoPath, duration);
                cursor.close();
            }
        }
    }

    private void onVideoChooseResult(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Video.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        if (filePath != null && filePath.length() > 0)
            onVideoChoosed(filePath, 0);
    }

    private void onRecordResult(Intent data) {
        Uri selectedAudio = data.getData();
        String[] filePathColumn = { MediaStore.Audio.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(selectedAudio,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        if (filePath != null && filePath.length() > 0)
            onAudioChoosed(filePath);
    }

    private void onMusicStoreResult(Intent data) {
        if (data == null) {
            @SuppressWarnings("deprecation")
            Cursor cursor = getActivity().managedQuery(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media.DURATION }, null, null,
                    MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                final String audioPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                final long duration = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION));
                onAudioChoosed(audioPath);
            }
        } else {
            Uri audioUri = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = getActivity().managedQuery(audioUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final String audioPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                final long duration = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION));
                onAudioChoosed(audioPath);
            }
        }
    }

    protected void onPictureChoosed(String filePath) {

    }

    protected void onVideoChoosed(String videoPath, long duration) {

    }

    protected void onAudioChoosed(String audioPath) {

    }

    protected void onScanResult(String scanResult) {

    }

    protected MediaPlayer requestPlayVoice(String path,
                                           PlayVoiceListenerWrapper listener) {
        MediaPlayer mp = new MediaPlayer();
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(listener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        mp.setLooping(false);
        mp.setOnCompletionListener(listener);
        mp.setOnErrorListener(listener);
        mp.setOnInfoListener(listener);
        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
            return mp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface PlayVoiceListenerWrapper extends MediaPlayer.OnErrorListener,
            MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, AudioManager.OnAudioFocusChangeListener {

    }

    protected void createListDialog(final String[] menus, String titleText,
                                    final CallBack callBack) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setItems(menus, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onItemClick(dialog, menus[which],which);
                    }
                })
                .setTitle(titleText==null?"":titleText)
                .setNegativeButton(
                        getActivity().getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    protected void createSingleDialog(final String[] menus, String titleText,
                                      final CallBack callBack) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(menus, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                callBack.onItemClick(dialog, menus[whichButton],whichButton);
                            }
                        })
                .setTitle(titleText==null?"":titleText)
                .setNegativeButton(
                        getActivity().getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    protected void createMultiChoiceDialog(final String[] menus, String titleText,
                                           final MultiCallBack callBack) {
        final ArrayList <Integer> MultiChoiceID = new ArrayList <Integer>();
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMultiChoiceItems(menus,
                        new boolean[]{false, false, false, false, false, false, false},
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                                if(isChecked) {
                                    MultiChoiceID.add(whichButton);
                                    //   你选择的为：mItems[whichButton]   
                                }else {
                                    MultiChoiceID.remove(whichButton);
                                }
                                callBack.onItemClick(dialog, MultiChoiceID);
                            }
                        })
                .setTitle(titleText==null?"":titleText)
                .setNegativeButton(
                        getActivity().getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    public interface CallBack {
        void onItemClick(DialogInterface dialog, String data,int postion);
    }

    public interface MultiCallBack {
        void onItemClick(DialogInterface dialog, List<Integer> list);
    }

    protected void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        showDatePicker(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
    }

    protected void showDatePicker(long maxDate, long minDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        showDatePicker(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), maxDate, minDate);
    }

    protected void showDatePicker(int year, int month, int day) {
        showDatePicker(year, month, day, 0, 0);
    }

    protected void showDatePicker(int year, int month, int day, long maxDate,
                                  long minDate) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                onDateChooseResult(cal);
            }
        };
        DatePickerDialog d = new DatePickerDialog(getActivity(), listener, year, month,
                day);
        d.setCanceledOnTouchOutside(false);
        d.setTitle(getResources().getString(R.string.choose_date));
        if (maxDate > 0) {
            d.getDatePicker().setMaxDate(maxDate);
        }
        if (minDate > 0) {
            d.getDatePicker().setMinDate(minDate);
        }
        d.show();
    }

    protected void onDateChooseResult(Calendar cal) {

    }

    protected void showTimePicker() {
        Calendar c = Calendar.getInstance();
        showTimePicker(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                true);
    }

    protected void showTimePicker(int hourOfDay, int minute,
                                  boolean is24HourView) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                onTimeChooseResult(hourOfDay, minute);
            }
        };

        TimePickerDialog d = new TimePickerDialog(getActivity(), listener, hourOfDay,
                minute, is24HourView);
        d.setCanceledOnTouchOutside(false);
        d.setTitle(getResources().getString(R.string.choose_time));
        d.show();
    }

    protected void onTimeChooseResult(int hourOfDay, int minute) {
    }


    protected void pushEvent(Event event, OnEventRunner runner) {
        pushEvent(event, runner, true);
    }

    protected void pushEvent(Event event,  OnEventRunner runner,boolean isShowLoadingDialog) {
        xEventManager.pushEvent(event, this, runner);
        if(isShowLoadingDialog)
            showDialogForLoading();
    }

    @Override
    public void onEventRunEnd(Event event) {
        hideDialogForLoading();
    }

    protected void launchNativePlayer(Uri uri) {
        // Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/mp4");
        startActivity(intent);
    }

    protected Dialog createDialogForLoading() {
        return mSimpleUIFractory.createLoadingDialog();
    }

    protected void createProgressDialog(int title, int progress) {
        mProgressDialog = mSimpleUIFractory.createProgressDialog(
                title == 0 ? null : getString(title));
    }
    protected void setProgressDialog(int progress){
        mProgressDialog.setProgress(progress);
    }


    protected void showYesNoDialog(int yesTextId, int noTextId, int title,
                                   int msgTextId, DialogInterface.OnClickListener listener) {
        showDialog(R.string.sure, R.string.cancel, title, msgTextId, listener);
    }

    protected void showDialog(int yesTextId, int noTextId, int title,
                              int msgTextId, DialogInterface.OnClickListener listener) {
        Dialog d = mSimpleUIFractory.createYesNoDialog(getString(yesTextId),
                noTextId == 0 ? null : getString(noTextId), title == 0 ? null
                        : getString(title), msgTextId == 0 ? null
                        : getString(msgTextId), listener);
        d.show();
    }

    protected void showDialogForLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    protected void hideDialogForLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.cancel();
        }
    }

    private void initNetworkStateListener() {
        networkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkState(boolean networkIsAvailable,
                                       NetworkUtil.NetworkType networkType) {
                if(networkIsAvailable){
                    removeConnectionPromptView();
                    reloadData();
                }
                onNetWorkStateListener(networkIsAvailable, networkType);
            }
        };
        NetworkStateReceiver.addNetworkStateListener(networkStateListener);
        addConnectionPromptView();
    }

    protected void onNetWorkStateListener(boolean networkIsAvailable,
                                          NetworkUtil.NetworkType networkType){

    }

    protected void addConnectionPromptView() {
        if (mViewPromptConnection == null) {
            mViewPromptConnection = LayoutInflater.from(getActivity()).inflate(
                    R.layout.common_net_prompt, null);
            getActivity().addContentView(mViewPromptConnection, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mViewPromptConnection.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    reloadData();
                }
            });
        } else {
            mViewPromptConnection.setVisibility(View.VISIBLE);
        }
    }

    private void removeConnectionPromptView() {
        if (mViewPromptConnection != null) {
            mViewPromptConnection.setVisibility(View.GONE);
        }
    }

    protected void reloadData() {
        if (NetworkUtil.isNetworkConnected(getActivity()))
            removeConnectionPromptView();
        else
            return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networkStateListener != null) {
            NetworkStateReceiver
                    .removeNetworkStateListener(networkStateListener);
        }
    }
}
