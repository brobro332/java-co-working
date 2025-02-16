import React, { useCallback, useEffect, useState } from "react";
import { Box, Button, TextField, Table, TableContainer, Pagination, TableCell, TableBody, TableRow, TableHead, Paper } from "@mui/material";
import axios from "axios";

const Request = () => {
  const [name, setName] = useState('');
  const [workspaceList, setWorkspaceList] = useState([]); 
  const [page, setPage] = useState(1);

  const pageSize = 10;
  const startIndex = (page - 1) * pageSize;
  const currentPageData = workspaceList.slice(startIndex, startIndex + pageSize);

  const fetchWorkspaceListNotJoined = useCallback(async () => {
    try {
      const result = await axios.get(
        "http://localhost:8080/api/v1/workspace/workspaceList-not-joined",
        {
          params: { 
            name: name?.trim(),
          },
          withCredentials: true,
        }
      );
      if (result.status === 200) {
        const workspaceList = result.data.data;

        setPage(1);
        setWorkspaceList(workspaceList);
      }
    } catch (e) {
      console.error(e);
    }
  }, [name]);

  useEffect(() => {
    fetchWorkspaceListNotJoined();
  }, [fetchWorkspaceListNotJoined]);

  const createInvitation = async (row) => {
    try {
      const result = await axios.post(
        "http://localhost:8080/api/v1/invitation",
        {
          workspaceId: row.id,
          requesterType: 'MEMBER'
        },
        {
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          withCredentials: true
        }
      );

      if (result.status === 200) {
        fetchWorkspaceListNotJoined();
      }
    } catch (e) {
      console.error(e);
    }
  };

  const deleteInvitation = async (row) => {
    try {
      const result = await axios.delete(
        "http://localhost:8080/api/v1/invitation",
        {
          data: {
            id: row.invitationId
          },
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          withCredentials: true
        }
      );

      if (result.status === 200) {
        fetchWorkspaceListNotJoined();
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return (
    <Box>
      <Box display="flex" gap={2} sx={{ marginBottom:"15px" }}>
        <TextField 
          label="워크스페이스 이름" 
          variant="outlined" 
          size="small" 
          sx={{ flex: 1 }}
          value={name}
          onChange={(e) => setName(e.target.value)} 
        />
      </Box>
      <Box>
        <TableContainer component={Paper}>
          <Table sx={{ tableLayout: "fixed", width: "100%" }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ width: "20%" }}>워크스페이스 이름</TableCell>
                <TableCell sx={{ width: "30%" }}>리더</TableCell>
                <TableCell sx={{ width: "40%" }}>소개</TableCell>
                <TableCell sx={{ width: "10%" }}>처리</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
            {currentPageData.map((workspace) => (
              <TableRow key={workspace.id} sx={{ height: "30px" }}>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {workspace.name}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {workspace.leader}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {workspace.description}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {workspace.invitationStatus === null ? (
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => createInvitation(workspace)}
                    >
                      신청
                    </Button>
                  ) : (
                    workspace.invitationStatus === 'PENDING' ? (
                      <Button
                        variant="contained"
                        color="error"
                        onClick={() => deleteInvitation(workspace)}
                      >
                        취소
                      </Button>
                    ) : (
                      workspace.invitationStatus === 'ACCEPTED' ? '가입완료' : '거절' 
                    ) 
                  )}
                </TableCell>
              </TableRow>
              ))}
            </TableBody>
          </Table>
          {workspaceList && (
            <Pagination
              count={Math.ceil(workspaceList.length / pageSize)}
              page={page}
              color="primary"
              onChange={handlePageChange}
              sx={{ height: "50px", display: "flex", justifyContent: "center" }}
            />
          )}
        </TableContainer>
      </Box>
    </Box>
  );
};

export default Request;