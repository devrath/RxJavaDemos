package com.demo.code.ui.operators.creationalOperators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.code.databinding.FragmentOperatorTimerBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TimerOperatorFragment : Fragment() {

    private val TAG = TimerOperatorFragment::class.java.simpleName
    private var _binding: FragmentOperatorTimerBinding? = null
    private val binding get() = _binding!!

    private val TIME_UNIT = TimeUnit.SECONDS
    private val MAXIMUM_PERIOD = 5L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperatorTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            subscribeToObservable()
        }
    }

    /**
     * Subscribe to the observable
     */
    private fun subscribeToObservable() {
        createObservable().subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                Timber.tag(TAG).d("Subscribe Invoked")
            }

            override fun onNext(t: Long) {
                Timber.tag(TAG).d("Value: %s", t)
            }

            override fun onError(e: Throwable) {
                Timber.tag(TAG).e("ERROR: %s",e.message)
            }

            override fun onComplete() {
                Timber.tag(TAG).d("Task is complete")
            }
        })
    }

    /**
     * Create the observable
     */
    private fun createObservable() : Observable<Long> {
        return Observable.timer(MAXIMUM_PERIOD,TIME_UNIT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
    }
}