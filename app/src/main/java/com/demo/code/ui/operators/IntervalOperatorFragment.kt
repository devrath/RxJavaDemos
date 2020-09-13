package com.demo.code.ui.operators

import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.code.databinding.FragmentOperatorIntervalBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class IntervalOperatorFragment : Fragment() {

    private val TAG = IntervalOperatorFragment::class.java.simpleName
    private var _binding: FragmentOperatorIntervalBinding? = null
    private val binding get() = _binding!!

    private val INTERVAL_PERIOD = 1L
    private val MAXIMUM_PERIOD = 5L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperatorIntervalBinding.inflate(inflater, container, false)
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
     * Create the observable
     */
    private fun createObservable(): Observable<Long> {
        return Observable.interval(INTERVAL_PERIOD, TimeUnit.SECONDS)
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .takeWhile { value ->
                             Timber.tag(TAG).d("Thread: %s",Thread.currentThread())
                             value <= MAXIMUM_PERIOD
                         }
    }

    /**
     * Subscribe to the observable
     */
    private fun subscribeToObservable() {
        createObservable().subscribe(object : Observer<Long>{
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
}