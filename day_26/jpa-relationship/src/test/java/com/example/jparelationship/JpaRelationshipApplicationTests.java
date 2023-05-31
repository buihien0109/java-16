package com.example.jparelationship;

import com.example.jparelationship.entity.Customer;
import com.example.jparelationship.entity.Order;
import com.example.jparelationship.entity.User;
import com.example.jparelationship.repository.CustomerRepository;
import com.example.jparelationship.repository.OrderRepository;
import com.example.jparelationship.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class JpaRelationshipApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void save_customer() {
        for (int i = 0; i < 2; i++) {
            Customer customer = Customer.builder()
                    .name("customer " + (i + 1))
                    .build();
            customerRepository.save(customer);

            for (int j = 0; j < 3; j++) {
                Order order = Order.builder()
                        .orderNumber(i + 1)
                        .customer(customer)
                        .build();

                orderRepository.save(order);
            }
        }
    }

    @Test
    void delete_customer() {
        customerRepository.deleteById(1);
    }

    @Test
    @Transactional
    void add_customer() {
        List<Order> orderList = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            Order order = Order.builder()
                    .orderNumber(j + 1)
                    .build();
            orderList.add(order);
        }

        // TODO : Kiểm tra xem tại sao khi lưu customer và order mà customer_id = null
        Customer customer = Customer.builder()
                .name("customer 7")
                .orders(orderList)
                .build();

        customerRepository.save(customer);
    }

    @Test
    @Transactional
    void demo_removal() {
        Customer customer = customerRepository.findById(2).orElse(null);
        List<Order> orderList = customer.getOrders();

        // TODO : Tại sao khi xóa 1 phần khỏi list không remove khỏi cơ sở dữ liệu?
        System.out.println(orderList.get(0));
        orderList.remove(orderList.get(0));
    }

    @Test
    @Transactional
    void demo_fetch_type() {
        Customer customer = customerRepository.findById(2).orElse(null);

        // TODO: Đối với fetch LAZY, khi truy cập vào entity liên quan, mà không tiếp tục query
        List<Order> orderList = customer.getOrders();
        System.out.println(orderList.get(0));
    }

    @Test
    void save_user() {
        for (int i = 0; i < 2; i++) {
            User user = User.builder()
                    .name("user " + (i + 1))
                    .build();
            userRepository.save(user);
        }
    }

    /*
    Tạo 2 entity User, FileServer
     User:
        - id : Integer
        - name : String

     FileServer giữ khóa ngoại user_id
        - id : Integer
        - data : byte[]
        - createdAt : LocalDateTime
        - type : String

     Mối quan hệ : User - FileServer : one to many
    */
}
