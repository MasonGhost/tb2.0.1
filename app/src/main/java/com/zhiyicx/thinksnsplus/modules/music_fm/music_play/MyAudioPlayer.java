package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;


import java.io.File;
import java.io.FileDescriptor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.SeekBar;

/**
 * 
 * @ClassName:	MyMediaPlayer 
 * @Description:TODO(这里用一句话描述这个类的作用) 
 * @author:	legendary_tym
 * @date:	2016-6-16 下午1:49:21 
 *
 */
public class MyAudioPlayer implements OnBufferingUpdateListener,
OnCompletionListener, MediaPlayer.OnPreparedListener{

	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private Context mContext;
	public static boolean isPrepared=false;

	private OnAudioPlayerStatusChangeLitener mPlayerLitener;

	public interface OnAudioPlayerStatusChangeLitener{

		void onPlayeronCompletion();
		
		void onPlayerPrepared();

		void isPlaying(int progress);

	}

	public void setOnPlayerStatusChangeLitener(OnAudioPlayerStatusChangeLitener mPlayerLitener){
		this.mPlayerLitener=mPlayerLitener;
	}

	public MyAudioPlayer(SeekBar skbProgress,Context mContext)
	{

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);

		this.skbProgress=skbProgress;
		this.mContext=mContext;
	}

	public void playAudiofromAssets(String res){

		try { 
			mediaPlayerPrepare();
			AssetFileDescriptor aFD = mContext.getAssets().openFd(res);  
			FileDescriptor fileDescriptor = aFD.getFileDescriptor();  
			this.mediaPlayer.setDataSource(fileDescriptor, aFD.getStartOffset(), aFD.getLength());  
			aFD.close(); 
			PlayAudio(false);


		}catch (Exception e) {  
			// TODO: handle exception  
		}  
	}

	public void playAudiofromSDCard(String path){

		try {  
			mediaPlayerPrepare();
			//sdFile.getAbsoluteFile() + File.separator + "welcome.3gp"
			File sdFile = Environment.getExternalStorageDirectory();  
			this.mediaPlayer.setDataSource(path);  
			PlayAudio(false);
		}catch (Exception e) {  
			// TODO: handle exception  
		}  
	}

	public void playAudiofromInternet(String url,boolean isAsync){

		try {  
			mediaPlayerPrepare();
			this.mediaPlayer.setDataSource(mContext,Uri.parse(url));  
			PlayAudio(isAsync);
		}catch (Exception e) {  
			// TODO: handle exception  
		}  
	}

	public void playAudiofromResource(int resID){

		try {  
			mediaPlayerPrepare();
			this.mediaPlayer = MediaPlayer.create(mContext, resID);
			PlayAudio(false);
		}catch (Exception e) {  
			// TODO: handle exception  
		}  
	}

	public void PlayAudio(boolean isLargeRes){  

		try {  

			//准备完成后才可以播放,另外如果文件特别大或者从网上获得的资源  
			//会在这里等待时间过长,造成堵塞,这样的话就得用  
			if(isLargeRes){
				this.mediaPlayer.prepareAsync();
			}else{
				this.mediaPlayer.prepare();
			}               

		} catch (Exception e) {  
			// TODO: handle exception  
		}  
	}
	
	
	public void mediaPlayerPrepare(){
		if(mediaPlayer==null){
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
		}
	}


	public boolean isPlaying(){

		if(mediaPlayer!=null){
			return mediaPlayer.isPlaying();
		}
		return false;

	}



	public void reStart(){

		if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
		}else {

		}

	}

	public void start(){
		if(mediaPlayer!=null){
			mediaPlayer.start();
		}
	}

	public void pause()
	{
		if(mediaPlayer!=null){
			mediaPlayer.pause();
		}
	}

	public void stop()
	{
		if (mediaPlayer != null) { 
			isPrepared=false;
			skbProgress.setProgress(0);
			skbProgress.setSecondaryProgress(0);
			mediaPlayer.stop();
			mediaPlayer.release(); 
			mediaPlayer = null; 
		} 
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		mPlayerLitener.onPlayerPrepared();
		isPrepared=true;
		Log.e("MyAudioPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		mPlayerLitener.onPlayeronCompletion();
		skbProgress.setProgress(100);
		Log.e("MyAudioPlayer", "onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int duration=arg0.getDuration();
		if(duration!=0){
			int currentProgress=skbProgress.getMax()*arg0.getCurrentPosition()/duration;
			//		int currentProgress=skbProgress.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
			skbProgress.setProgress(currentProgress);
			Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
		}
	}

}
