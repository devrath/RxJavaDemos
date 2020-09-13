package com.demo.code.ui.operators

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.code.R
import com.demo.code.Utils.getArrayOfTasks
import com.demo.code.databinding.FragmentOperatorFromArrayBinding
import com.demo.code.models.Task
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

class FromArrayOperator : Fragment() {

    private val TAG = FromArrayOperator::class.java.simpleName
    private var _binding: FragmentOperatorFromArrayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperatorFromArrayBinding.inflate(inflater, container, false)
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
    private fun createObservable(): Observable<Array<Task>> {
        return Observable.fromArray(getArrayOfTasks())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Subscribe to the observable
     */
    private fun subscribeToObservable() {
        createObservable().subscribe(object : Observer<Array<Task>>{
            override fun onSubscribe(d: Disposable) {
                Timber.tag(TAG).d("Subscribe Invoked")
            }

            override fun onNext(t: Array<Task>) {
                Timber.tag(TAG).d("Value: %s", t.contentToString())
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