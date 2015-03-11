package org.triman.robotsimulator

trait RealtimeClock extends IClock {

  private var tickerThread : Thread = null
  
  private var hasToRun = false;
  
  override def start() : Unit = {
    this.synchronized {
      // do nothing if we're running
      if(tickerThread != null && tickerThread.isAlive()){
        return
      }
      
      hasToRun = true
      tickerThread = new Thread(new Runnable(){
        override def run(){
          var last = System.currentTimeMillis
          while(hasToRun){
            
            val current = System currentTimeMillis
            val dt = (current - last) / 1000.0
            last = current
            
            time() += dt
            
            try{
            	Thread.sleep(30);
            }catch{
              case _ : InterruptedException => return
            }
          }
        }
      })
      
      tickerThread.start()
      
    }
  }
  
  
  
  override def stop() : Unit = {
    this.synchronized{
    	hasToRun = false
    	tickerThread interrupt
    }
    
  }
  
  
}