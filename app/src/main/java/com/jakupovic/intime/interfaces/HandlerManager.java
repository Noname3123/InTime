package com.jakupovic.intime.interfaces;

import android.os.Handler;

/**this interface exposes (static) methods responsible for manipulating handlers
 * */
public interface HandlerManager {
    /**THis method takes a handler and removes all runnables connected to it
     * @params Handler
     * @return void
     */

    static void ResetHandler(Handler handler){
       try{
           handler.removeCallbacksAndMessages(null); //remove all callbacks from handler
       }
       catch(Exception e){
           return;
        }

    }
/**this method takes a handler, a runnable. The method posts a runnable to a handler
 * @param handler - a handler instance
 * @param toRun - a runnable which the handler executes
 * @return void
 * */
    static void RegisterHandlerFunction(Handler handler, Runnable toRun){

        handler.post(toRun);


    }


}
