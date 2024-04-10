package com.nick.api.service;

import com.nick.api.domain.Student;
import com.nick.api.mapper.StudentMapper;
import com.nick.common.exception.ServiceException;
import com.nick.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public List<Student> findUser(Student student) {
        return studentMapper.findUser(student);
    }

    public int addUser(Student student) {
        return studentMapper.addUser(student);
    }

    public int updateUser(Student student) {
        return studentMapper.updateUser(student);
    }

    public int deleteUserByIds(Long[] ids) {
        return studentMapper.deleteUserByIds(ids);
    }

    public void importUser(List<Student> list, Boolean isUpdateSupport) {
        if (StringUtils.isNull(list) || list.isEmpty()) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Student student : list) {
            studentMapper.addUser(student);
        }
    }

//            try
//            {
//                // 验证是否存在这个用户
//                SysUser u = studentMapper.selectUserByUserName(user.getUserName());
//                if (StringUtils.isNull(u))
//                {
//                    BeanValidators.validateWithException(validator, user);
//                    user.setPassword(SecurityUtils.encryptPassword(password));
//                    user.setCreateBy(operName);
//                    userMapper.insertUser(user);
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
//                }
//                else if (isUpdateSupport)
//                {
//                    BeanValidators.validateWithException(validator, user);
//                    checkUserAllowed(u);
//                    checkUserDataScope(u.getUserId());
//                    user.setUserId(u.getUserId());
//                    user.setUpdateBy(operName);
//                    userMapper.updateUser(user);
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
//                }
//                else
//                {
//                    failureNum++;
//                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
//                }
//            }
//            catch (Exception e)
//            {
//                failureNum++;
//                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
//                failureMsg.append(msg + e.getMessage());
//                log.error(msg, e);
//            }
//        }
//        if (failureNum > 0)
//        {
//            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
//            throw new ServiceException(failureMsg.toString());
//        }
//        else
//        {
//            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
//        }
}
