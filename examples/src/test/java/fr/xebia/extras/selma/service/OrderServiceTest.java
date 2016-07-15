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

import fr.xebia.extras.selma.beans.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * User: slemesle
 * Date: 27/11/2013
 */
public class OrderServiceTest {

    @Test
    public void should_map_order() {

        Order in = new Order();
        in.setCreationDate(new Date());
        in.setId(new BigInteger("1234567876543236789876543"));
        in.setSalesChannel(SalesChannel.WEB);
        in.setTotalAmount(4242424);
        in.setCustomer(new Customer());
        in.getCustomer().setEmail("custom@er.fr");
        in.getCustomer().setName("John Doe");
        in.getCustomer().setPhoneNumber("+33123435676");
        in.getCustomer().setAddress(new Address());
        in.getCustomer().getAddress().setCityLine("Paris 75015");
        in.getCustomer().getAddress().setStreetLine1("420 rue Vaugirard");
        in.getCustomer().setDate("123467887654323");
        in.setProducts(new ArrayList<Product>());
        in.getProducts().add(new Product());
        in.getProducts().get(0).setCode("4242");
        in.getProducts().get(0).setLabel("Pretty thing");
        in.getProducts().get(0).setLogisticMap(new HashMap<String, String>());
        in.getProducts().get(0).getLogisticMap().put("key", "value");
        in.getProducts().get(0).setPrice(4242.42d);
        in.getProducts().get(0).setTags(new HashSet<String>());
        in.getProducts().get(0).getTags().add("Geek");
        in.getProducts().get(0).getTags().add("Gadget");
        in.getProducts().get(0).getTags().add("Electronic");
        in.getProducts().get(0).setType(ProductType.SERVICE);

        final OrderService service = new OrderService();

        OrderDto res = service.process(in);


        Assert.assertEquals(in.getCreationDate(), res.getCreationDate());
        Assert.assertEquals(in.getSalesChannel().toString(), res.getSalesChannel().toString());
        Assert.assertEquals(in.getTotalAmount(), res.getTotalAmount());
        Assert.assertEquals(in.getCustomer().getEmail(), res.getCustomer().getEmail());
        Assert.assertEquals(in.getCustomer().getPhoneNumber(), res.getCustomer().getPhoneNumber());
        Assert.assertEquals(in.getCustomer().getName(), res.getCustomer().getName());
        Assert.assertEquals(in.getCustomer().getDate(), res.getCustomer().getDate() + "");
        Assert.assertEquals(in.getCustomer().getAddress().getCityLine(), res.getCustomer().getAddress().getCityLine());
        Assert.assertEquals(in.getCustomer().getAddress().getStreetLine1(), res.getCustomer().getAddress().getStreetLine1());
        Assert.assertEquals(in.getCustomer().getAddress().getStreetLine2(), res.getCustomer().getAddress().getStreetLine2());
        Assert.assertEquals(in.getProducts().size(), res.getProducts().size());
        Assert.assertEquals(in.getProducts().get(0).getTags(), res.getProducts().get(0).getTags());
        Assert.assertEquals(in.getProducts().get(0).getCode(), res.getProducts().get(0).getCode());
        Assert.assertEquals(in.getProducts().get(0).getLabel(), res.getProducts().get(0).getLabel());
        Assert.assertEquals(in.getProducts().get(0).getPrice(), res.getProducts().get(0).getPrice(), 0d);
        Assert.assertEquals(in.getProducts().get(0).getType().toString(), res.getProducts().get(0).getType());


    }

    @Test
    public void should_map_reverse_order() {

        OrderDto in = new OrderDto();
        in.setCreationDate(new Date());
        in.setSalesChannel(SalesChannelDto.WEB);
        in.setTotalAmount(4242424);
        in.setCustomer(new CustomerDto());
        in.getCustomer().setEmail("custom@er.fr");
        in.getCustomer().setName("John Doe");
        in.getCustomer().setPhoneNumber("+33123435676");
        in.getCustomer().setAddress(new AddressDto());
        in.getCustomer().getAddress().setCityLine("Paris 75015");
        in.getCustomer().getAddress().setStreetLine1("420 rue Vaugirard");
        in.getCustomer().setDate(new DateBO("123467887654323"));
        in.setProducts(new ArrayList<ProductDto>());
        in.getProducts().add(new ProductDto());
        in.getProducts().get(0).setCode("4242");
        in.getProducts().get(0).setLabel("Pretty thing");
        in.getProducts().get(0).setPrice(4242.42d);
        in.getProducts().get(0).setTags(new HashSet<String>());
        in.getProducts().get(0).getTags().add("Geek");
        in.getProducts().get(0).getTags().add("Gadget");
        in.getProducts().get(0).getTags().add("Electronic");
        in.getProducts().get(0).setType(ProductType.SERVICE.toString());

        final OrderService service = new OrderService();

        Order res = service.update(in);


        Assert.assertEquals(in.getCreationDate(), res.getCreationDate());
        Assert.assertEquals(in.getSalesChannel().toString(), res.getSalesChannel().toString());
        Assert.assertEquals(in.getTotalAmount(), res.getTotalAmount());
        Assert.assertEquals(in.getCustomer().getEmail(), res.getCustomer().getEmail());
        Assert.assertEquals(in.getCustomer().getPhoneNumber(), res.getCustomer().getPhoneNumber());
        Assert.assertEquals(in.getCustomer().getName(), res.getCustomer().getName());
        Assert.assertEquals(in.getCustomer().getDate() + "", res.getCustomer().getDate() + "");
        Assert.assertEquals(in.getCustomer().getAddress().getCityLine(), res.getCustomer().getAddress().getCityLine());
        Assert.assertEquals(in.getCustomer().getAddress().getStreetLine1(), res.getCustomer().getAddress().getStreetLine1());
        Assert.assertEquals(in.getCustomer().getAddress().getStreetLine2(), res.getCustomer().getAddress().getStreetLine2());
        Assert.assertEquals(in.getProducts().size(), res.getProducts().size());
        Assert.assertEquals(in.getProducts().get(0).getTags(), res.getProducts().get(0).getTags());
        Assert.assertEquals(in.getProducts().get(0).getCode(), res.getProducts().get(0).getCode());
        Assert.assertEquals(in.getProducts().get(0).getLabel(), res.getProducts().get(0).getLabel());
        Assert.assertEquals(in.getProducts().get(0).getPrice(), res.getProducts().get(0).getPrice(), 0d);
        Assert.assertEquals(in.getProducts().get(0).getType(), res.getProducts().get(0).getType().toString());


    }

}
