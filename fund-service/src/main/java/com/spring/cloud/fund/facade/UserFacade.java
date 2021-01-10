package com.spring.cloud.fund.facade;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.spring.cloud.common.vo.User;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(value = "user-service", fallbackFactory = UserFacade.UserFacadeFallBackFactory.class)
public interface UserFacade {


    @GetMapping("/user/{id}")
    User getUser(@PathVariable("id") Long id);

    @GetMapping("/users/{timeout}")
    List<User> getUsers(@PathVariable("timeout") Long timeout);

    /**
     * hystrix.command.UserFacade#getAllUsers(Long)
     * hystrix.threadpool.user-service
     * @param timeout
     * @return
     */
    @GetMapping("/users/{timeout}")
    HystrixCommand<List<User>> getAllUsers(@PathVariable("timeout") Long timeout);

    @Component
    class UserFacadeFallBackFactory implements FallbackFactory<UserFacade> {

        @Override
        public UserFacade create(Throwable throwable) {
            throwable.printStackTrace();
            return new UserFacade() {
                private final Setter hystrixCommandGroupKey =
                        HystrixCommand.Setter.withGroupKey(
                                HystrixCommandGroupKey.Factory.asKey("UserFacade"));

                @Override
                public User getUser(Long id) {

                    return new User(0L, "default", "null");
                }

                @Override
                public List<User> getUsers(Long timeout) {
                    System.out.println("fallback: getUsers(" + timeout + ")");
                    return Collections.emptyList();
                }

                /**
                 * hystrix.command.UserFacade$UserFacadeFallBackFactory$1$1
                 * hystrix.threadpool.UserFacade
                 * @param timeout
                 * @return
                 */
                @Override
                public HystrixCommand<List<User>> getAllUsers(Long timeout) {
                    System.out.println("fallback: getAllUsers(" + timeout + ")");
                    return new HystrixCommand<List<User>>(hystrixCommandGroupKey) {
                        @Override
                        protected List<User> run() throws Exception {
                            System.out.println("fallback: getAllUsers(" + timeout + ")");
                            return Collections.emptyList();
                        }
                    };
                }
            };
        }
    }
}
