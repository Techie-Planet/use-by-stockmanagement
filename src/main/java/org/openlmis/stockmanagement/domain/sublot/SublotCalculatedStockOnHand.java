/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.stockmanagement.domain.sublot;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.openlmis.stockmanagement.domain.BaseEntity;
import org.openlmis.stockmanagement.domain.sublot.SublotStockCard;

@Data
@Entity
@Table(name = "sublot_calculated_stocks_on_hand", schema = "stockmanagement")
public class SublotCalculatedStockOnHand extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "sublot_stock_card_id", referencedColumnName = "id", nullable = false)
    private SublotStockCard sublotStockCard;
    @Column(nullable = false)
    private Integer sublotStockOnHand;
    @Column(nullable = false)
    private LocalDate occurredDate;

    @Column(nullable = false, columnDefinition = "timestamp")
    private ZonedDateTime processedDate;
}