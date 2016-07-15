/*
 * Copyright 2013 Xebia and Séven Le Mesle
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
 *
 */
package fr.xebia.extras.selma.service;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.Order;
import fr.xebia.extras.selma.beans.Order2OrderDto;
import fr.xebia.extras.selma.beans.OrderDto;

/**
 *
 */
public class OrderService {

    private final Order2OrderDto orderMapper;

    public OrderService() {
        this.orderMapper = Selma.getMapper(Order2OrderDto.class);
    }

    public OrderDto process(Order order) {
        return orderMapper.asDto(order);
    }

    public Order update(OrderDto dto) {
        return orderMapper.asOrder(dto);
    }

}
