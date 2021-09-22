package com.example.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.common.model.CustomUserDetails;
import com.example.security.entity.User;
import com.example.security.repo.UserRepo;

public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> oUser = userRepo.findByNameIgnoreCase(username);
    if (oUser.isPresent()) {
      User u = oUser.get();
      CustomUserDetails usr = new CustomUserDetails(u.getId(), u.getName(), u.getDisplayName(), u.getPassword(),
          u.isActive(), u.isLock(), u.getAffectDate(), u.getExpireDate(), u.getTenantId());
      usr.setAuthorities(getAuthorities(u));
      return usr;
    } else {
      throw new UsernameNotFoundException(username + " not found");
    }

  }

  private List<GrantedAuthority> getAuthorities(User user) {
    List<GrantedAuthority> authorities = new ArrayList<>();
//    HashMap<String, SecurableAsset> securableAssets = new HashMap<>();
//    HashSet<UUID> idSet = new HashSet<>();
//    idSet.add(user.getGuid());
//    List<UUID> checkedList = new ArrayList<>();
//    List<Group> gList = userSvc.getDirectContainers(user.getGuid());
//    while (gList.size() > 0) {
//      Group g = gList.get(0);
//      gList.remove(0);
//      if (g.isActive() && !checkedList.contains(g.getGuid())) {
//        checkedList.add(g.getGuid());
//        idSet.add(g.getGuid());
//        List<Group> l = userSvc.getDirectContainers(g.getGuid());
//        gList.addAll(l);
//      }
//    }
//    List<UUID> idList = new ArrayList<>(idSet);
//    List<Permission> perList = perRepo.findBySecurityObjectGuidIn(idList);
//    List<UUID> checkList = new ArrayList<>();
//    for (Permission per : perList) {
//      if (!per.isActive())
//        continue;
//      checkList.add(per.getSecurableAssetGuid());
//    }
//    while (checkList.size() > 0) {
//      UUID uuid = checkList.get(0);
//      String key = uuid.toString();
//      if (!securableAssets.containsKey(key)) {
//        SecurableAsset sa = saRepo.getOne(uuid);
//        authorities.add(new SimpleGrantedAuthority(sa.getName()));
//        securableAssets.put(key, sa);
//
//        List<SecurableAsset> childList = saRepo.findByParentSecurableAssetGuid(uuid);
//        for (SecurableAsset child : childList) {
//          checkList.add(child.getGuid());
//        }
//      }
//      checkList.remove(0);
//    }
    return authorities;
  }

}
