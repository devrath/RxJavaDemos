package com.demo.code.ui.operators.transformationOperators

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.demo.code.Utils.getListOfTasks
import com.demo.code.databinding.FragmentOperatorMapBinding
import com.demo.code.models.Task
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MapOperatorFragment : Fragment() {

    private val TAG = MapOperatorFragment::class.java.simpleName
    private var _binding: FragmentOperatorMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperatorMapBinding.inflate(inflater, container, false)
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
        createObservable().subscribe(object : Observer<Task>{
            override fun onSubscribe(d: Disposable) {
                Timber.tag(TAG).d("Subscribe Invoked")
            }

            override fun onNext(t: Task) {
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
    private fun createObservable(): Observable<Task> {
        return Observable.fromIterable(getListOfTasks())
                    .subscribeOn(Schedulers.io())
                    .map(object : Function<Task,Task>{
                        override fun apply(t: Task): Task {
                            t.taskIsCompleted = true
                            return t
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())

    }

}