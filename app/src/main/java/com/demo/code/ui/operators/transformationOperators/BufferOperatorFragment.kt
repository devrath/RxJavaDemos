package com.demo.code.ui.operators.transformationOperators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.code.utilities.Utils
import com.demo.code.databinding.FragmentOperatorBufferBinding
import com.demo.code.models.Task
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import android.view.View as View1

class BufferOperatorFragment : Fragment() {

    private val TAG = BufferOperatorFragment::class.java.simpleName
    private var _binding: FragmentOperatorBufferBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View1? {
        _binding = FragmentOperatorBufferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View1, savedInstanceState: Bundle?) {
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
    private fun createObservable() : Observable<MutableList<Task>> {
        return Observable.fromIterable(Utils.getListOfTasks())
            .subscribeOn(Schedulers.io())
            .buffer(2)
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Subscribe to the observable
     */
    private fun subscribeToObservable() {
        createObservable().subscribe(object : Observer<MutableList<Task>> {
            override fun onSubscribe(d: Disposable) {
                Timber.tag(TAG).d("Subscribe Invoked")
            }

            override fun onNext(t: MutableList<Task>) {
                Timber.tag(TAG).d("onNext: bundle results: -------------------");
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