package com.future.pms.admin.splash.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.core.base.BaseActivity
import com.future.pms.admin.core.model.Token
import com.future.pms.admin.core.network.Authentication
import com.future.pms.admin.databinding.ActivitySplashBinding
import com.future.pms.admin.login.view.LoginActivity
import com.future.pms.admin.main.view.MainActivity
import com.future.pms.admin.splash.injection.DaggerSplashComponent
import com.future.pms.admin.splash.injection.SplashComponent
import com.future.pms.admin.splash.presenter.SplashPresenter
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract {
  private var daggerBuild: SplashComponent = DaggerSplashComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: SplashPresenter
  private lateinit var binding: ActivitySplashBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    val settings = getSharedPreferences("prefs", 0)
    val firstRun = settings.getBoolean("firstRun", false)
    if (!firstRun) {
      presenter.attach(this)
      if (isOnline()) {
        checkAuthenticated()
      } else {
        binding.ibRefresh.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_LONG).show()
        binding.ibRefresh.setOnClickListener {
          if (isOnline()) {
            binding.ibRefresh.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            checkAuthenticated()
          } else {
            Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_LONG).show()
          }
        }
      }
    } else {
      val a = Intent(this, Dispatchers.Main::class.java)
      startActivity(a)
    }
  }

  override fun onSuccess(token: Token) {
    Authentication.save(this, token)
    goToHomePage()
  }

  private fun onAuthenticated() {
    goToHomePage()
  }

  private fun goToHomePage() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun onLogin() {
    showLogin()
  }

  override fun isAuthenticated(): Context? {
    return applicationContext
  }

  private fun showLogin() {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun checkAuthenticated() {
    try {
      if (Authentication.isAuthenticated(isAuthenticated())) {
        onAuthenticated()
      } else {
        refreshToken()
      }
    } catch (e: Authentication.WithoutAuthenticatedException) {
      onLogin()
    }
  }

  override fun refreshToken() {
    presenter.refreshToken(Authentication.getRefresh(this))
  }

  private fun isOnline(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetwork = cm?.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
  }

  override fun onFailed(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    showLogin()
  }
}