/*
 * Copyright (C) 2019. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uber.autodispose

import io.reactivex.BackpressureStrategy.ERROR
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.MaybeSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test

class AutoDisposeKotlinTest {

  companion object {
    private const val DEFAULT_PARALLELISM = 2
  }

  private val o = TestObserver<String>()
  private val s = TestSubscriber<String>()
  private val scope = CompletableSubject.create()
  private val scopeProvider = TestScopeProvider.create()

  @Test fun observable_maybeNormalCompletion() {
    Observable.just("Hello")
        .autoDispose(scope)
        .subscribe(o)

    o.assertValue { it == "Hello" }
    o.assertComplete()
  }

  @Test fun observable_maybeNormalInterrupted() {
    val subject = PublishSubject.create<String>()
    subject
        .autoDispose(scope)
        .subscribe(o)

    subject.onNext("Hello")

    o.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun observable_scopeProviderNormalCompletion() {
    Observable.just("Hello")
        .autoDispose(scopeProvider)
        .subscribe(o)

    o.assertValue { it == "Hello" }
    o.assertComplete()
  }

  @Test fun observable_scopeProviderNormalInterrupted() {
    val subject = PublishSubject.create<String>()
    subject
        .autoDispose(scopeProvider)
        .subscribe(o)

    subject.onNext("Hello")

    o.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun flowable_maybeNormalCompletion() {
    Flowable.just("Hello")
        .autoDispose(scope)
        .subscribe(s)

    s.assertValue { it == "Hello" }
    s.assertComplete()
  }

  @Test fun flowable_maybeNormalInterrupted() {
    val subject = PublishSubject.create<String>()
    subject.toFlowable(ERROR)
        .autoDispose(scope)
        .subscribe(s)

    subject.onNext("Hello")

    s.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(s.isDisposed).isTrue()
//    s.assertNotSubscribed()
  }

  @Test fun flowable_scopeProviderNormalCompletion() {
    Flowable.just("Hello")
        .autoDispose(scopeProvider)
        .subscribe(s)

    s.assertValue { it == "Hello" }
    s.assertComplete()
  }

  @Test fun flowable_scopeProviderNormalInterrupted() {
    val subject = PublishSubject.create<String>()
    subject.toFlowable(ERROR)
        .autoDispose(scopeProvider)
        .subscribe(s)

    subject.onNext("Hello")

    s.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(s.isDisposed).isTrue()
//    s.assertNotSubscribed()
  }

  @Test fun maybe_maybeNormalCompletion() {
    Maybe.just("Hello")
        .autoDispose(scope)
        .subscribe(o)

    o.assertValue { it == "Hello" }
    o.assertComplete()
  }

  @Test fun maybe_maybeNormalInterrupted() {
    val subject = MaybeSubject.create<String>()
    subject
        .autoDispose(scope)
        .subscribe(o)

    subject.onSuccess("Hello")

    o.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun maybe_scopeProviderNormalCompletion() {
    Maybe.just("Hello")
        .autoDispose(scopeProvider)
        .subscribe(o)

    o.assertValue { it == "Hello" }
    o.assertComplete()
  }

  @Test fun maybe_scopeProviderNormalInterrupted() {
    val subject = MaybeSubject.create<String>()
    subject
        .autoDispose(scopeProvider)
        .subscribe(o)

    scopeProvider.emit()

    subject.onSuccess("Hello")

    o.assertNoValues()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun single_maybeNormalCompletion() {
    Single.just("Hello")
        .autoDispose(scope)
        .subscribe(o)

    o.assertValue { it == "Hello" }
    o.assertComplete()
  }

  @Test fun single_maybeNormalInterrupted() {
    val subject = SingleSubject.create<String>()
    subject
        .autoDispose(scope)
        .subscribe(o)

    subject.onSuccess("Hello")

    o.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun single_scopeProviderNormalCompletion() {
    Single.just("Hello")
        .autoDispose(scopeProvider)
        .subscribe(o)

    o.assertValue { it == "Hello" }
    o.assertComplete()
  }

  @Test fun single_scopeProviderNormalInterrupted() {
    val subject = SingleSubject.create<String>()
    subject
        .autoDispose(scopeProvider)
        .subscribe(o)

    subject.onSuccess("Hello")

    o.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun completable_maybeNormalCompletion() {
    Completable.complete()
        .autoDispose(scope)
        .subscribe(o)

    o.assertComplete()
  }

  @Test fun completable_maybeNormalInterrupted() {
    val subject = PublishSubject.create<String>()
    subject
        .autoDispose(scope)
        .subscribe(o)

    subject.onNext("Hello")

    o.assertValue { it == "Hello" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun completable_scopeProviderNormalCompletion() {
    Completable.complete()
        .autoDispose(scopeProvider)
        .subscribe(o)

    o.assertComplete()
  }

  @Test fun completable_scopeProviderNormalInterrupted() {
    val subject = CompletableSubject.create()
    subject
        .autoDispose(scopeProvider)
        .subscribe(o)

    subject.onComplete()

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(o.isDisposed).isTrue()
//    o.assertNotSubscribed()
  }

  @Test fun parallelFlowable_maybeNormalCompletion() {
    val s2 = TestSubscriber<String>()
    Flowable.just("Hello", "World")
        .parallel(DEFAULT_PARALLELISM)
        .autoDispose(scope)
        .subscribe(arrayOf(s, s2))

    s.assertValue { it == "Hello" }
    s2.assertValue { it == "World" }
    s.assertComplete()
    s2.assertComplete()
  }

  @Test fun parallelFlowable_maybeNormalInterrupted() {
    val subject = PublishSubject.create<String>()
    val s2 = TestSubscriber<String>()
    subject.toFlowable(ERROR)
        .parallel(DEFAULT_PARALLELISM)
        .autoDispose(scope)
        .subscribe(arrayOf(s, s2))

    subject.onNext("Hello")
    subject.onNext("World")

    s.assertValue { it == "Hello" }
    s2.assertValue { it == "World" }

    scope.onComplete()

    // https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(s.isDisposed).isTrue()
//    s.assertNotSubscribed()
  }

  @Test fun parallelFlowable_scopeProviderNormalCompletion() {
    val s2 = TestSubscriber<String>()
    Flowable.just("Hello", "World")
        .parallel(DEFAULT_PARALLELISM)
        .autoDispose(scopeProvider)
        .subscribe(arrayOf(s, s2))

    s.assertValue { it == "Hello" }
    s.assertComplete()
    s2.assertValue { it == "World" }
    s2.assertComplete()
  }

  @Test fun parallelFlowable_scopeProviderNormalInterrupted() {
    val subject = PublishSubject.create<String>()
    val s2 = TestSubscriber<String>()
    subject.toFlowable(ERROR)
        .parallel(DEFAULT_PARALLELISM)
        .autoDispose(scopeProvider)
        .subscribe(arrayOf(s, s2))

    subject.onNext("Hello")
    subject.onNext("World")

    s.assertValue { it == "Hello" }
    s2.assertValue { it == "World" }

    scope.onComplete()

// https://github.com/ReactiveX/RxJava/issues/5178
//    assertThat(s.isDisposed).isTrue()
//    s.assertNotSubscribed()
  }
}
