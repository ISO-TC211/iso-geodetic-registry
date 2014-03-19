package org.iso.registry.api.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;

public class RegistryUserDetailsManager implements UserDetailsManager, GroupManager
{
	@Autowired
	private RegistryUserRepository userRepository;
	
	public RegistryUserDetailsManager() {
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		RegistryUser user = userRepository.findByEmailAddress(username.toLowerCase());
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}

		return user;
	}

	@Override
	public void createUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		return userRepository.findByEmailAddress(username.toLowerCase()) != null;
	}

	@Override
	public List<String> findAllGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findUsersInGroup(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createGroup(String groupName, List<GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroup(String groupName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameGroup(String oldName, String newName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUserToGroup(String username, String group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserFromGroup(String username, String groupName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GrantedAuthority> findGroupAuthorities(String groupName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addGroupAuthority(String groupName, GrantedAuthority authority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGroupAuthority(String groupName, GrantedAuthority authority) {
		// TODO Auto-generated method stub
		
	}
}
