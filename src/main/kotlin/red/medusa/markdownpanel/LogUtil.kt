package red.medusa.markdownpanel

import java.util.logging.Level
import java.util.logging.Logger


fun Logger.i(msg: String, method: String = "") = if (Profile.TEST) this.logp(Level.INFO, this.name, method, msg) else Unit
fun Logger.w(msg: String, method: String = "") = if (Profile.TEST) this.logp(Level.WARNING, this.name, method, msg) else Unit
fun Logger.e(msg: String, method: String = "")  = if (Profile.TEST) this.logp(Level.SEVERE, this.name, method, msg) else Unit

fun pt(message: String?) =  if (Profile.TEST) System.out.println(message) else Unit


