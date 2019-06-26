/*
 * Copyright (C) 2017 Moez Bhatti <moez.bhatti@gmail.com>
 *
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.moez.QKSMS.feature.blocking.numbers

import com.moez.QKSMS.common.base.QkPresenter
import com.moez.QKSMS.repository.BlockingRepository
import com.uber.autodispose.kotlin.autoDisposable
import javax.inject.Inject

class BlockedNumbersPresenter @Inject constructor(
    private val blockingRepo: BlockingRepository
) : QkPresenter<BlockedNumbersView, BlockedNumbersState>(
        BlockedNumbersState(numbers = blockingRepo.getBlockedNumbers())
) {

    override fun bindIntents(view: BlockedNumbersView) {
        super.bindIntents(view)

        view.unblockAddress()
                .doOnNext(blockingRepo::unblockNumber)
                .autoDisposable(view.scope())
                .subscribe()

        view.addAddress()
                .autoDisposable(view.scope())
                .subscribe { view.showAddDialog() }

        view.saveAddress()
                .autoDisposable(view.scope())
                .subscribe{ address -> blockingRepo.blockNumber(address) }
    }

}
