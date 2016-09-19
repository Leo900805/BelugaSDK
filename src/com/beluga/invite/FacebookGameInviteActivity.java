package com.beluga.invite;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.GameRequestContent.ActionType;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.GameRequestDialog.Result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FacebookGameInviteActivity extends Activity{
	
	GameRequestDialog requestDialog;
	CallbackManager callbackManager;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 FacebookSdk.sdkInitialize(this.getApplicationContext());
		 
		 Bundle params = new Bundle();
		 //params.putString("object", "{\"fb:app_id\":\"302184056577324\",\"og:type\":\"books.book\",\"og:url\":\"\u5c07\u4f60\u81ea\u5df1\u7684\u7db2\u5740\u7f6e\u5165\u5230\u9019\u88cf\u7684\u7269\u4ef6\",\"og:title\":\"Snow Crash\",\"og:image\":\"http:\/\/en.wikipedia.org\/wiki\/File:Snowcrash.jpg\",\"og:description\":\"In reality, Hiro Protagonist delivers pizza for Uncle Enzo\u2019s CosoNostra Pizza Inc., but in the Metaverse he\u2019s a warrior prince. Plunging headlong into the enigma of a new computer virus that\u2019s striking down hackers everywhere, he races along the neon-lit streets on a search-and-destroy mission for the shadowy virtual villain threatening to bring about infocalypse. Snow Crash is a mind-altering romp through a future America so bizarre, so outrageous\u2026you\u2019ll recognize it immediately.\",\"books:isbn\":\"0553380958\",\"books:rating:value\":\"Sample Rating: value\",\"books:rating:scale\":\"Sample Rating: scale\",\"books:author\":\"http:\/\/samples.ogp.me\/344562145628374\"}");
		 /* make the API call */
		 new GraphRequest(
		     AccessToken.getCurrentAccessToken(),
		     "/me/objects/books.book",
		     params,
		     HttpMethod.POST,
		     new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {
					// TODO Auto-generated method stub
					
					
				}
		     }
		 ).executeAsync();
		 
		  callbackManager = CallbackManager.Factory.create();
		  requestDialog = new GameRequestDialog(this);
		  requestDialog.registerCallback(callbackManager,
		    new FacebookCallback<GameRequestDialog.Result>() {
		   
		  
			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Result result) {
				// TODO Auto-generated method stub
				String id = result.getRequestId();
				Log.i("Game Invite", "id is :"+ id);
				
			}
			
		    }
		  );
		  
		  GameRequestContent content = new GameRequestContent.Builder()
				    .setMessage("Come play this level with me")
				    .setTo("793233194028541")
				    .setActionType(ActionType.SEND)
				    .setObjectId("1064480066970401")
				    .build();
				  requestDialog.show(content);
		
		  /*
		  GameRequestContent content = new GameRequestContent.Builder()
				    .setMessage("Come play this level with me")
				    .build();
				  requestDialog.show(content);
			*/	  
	}

}
