#include <iostream>
#include <mpi.h>
#include <stdio.h>
#include <string.h>
#include <vector>

void run_mpi_hello_world() {
    // rank proces
    int my_rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

    // Get the total number of processes
    int num_processes;
    MPI_Comm_size(MPI_COMM_WORLD, &num_processes);

    if (my_rank == 0) {
        // Process 0: Receive messages from all other processes
        std::string result = "";
        char buffer[256];

        for (int i = 1; i < num_processes; i++) {
            // Receive message from process i
            MPI_Recv(buffer, 256, MPI_CHAR, i, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            result += buffer;
            if (i < num_processes - 1) {
                result += "\n";
            }
        }

        // Display the final concatenated string
        std::cout << "Process 0 received all messages:\n" << result << std::endl;
    } else {
        // Other processes: Send a message to process 0
        char message[256];
        sprintf(message, "Hello from id %d", my_rank);
        MPI_Send(message, strlen(message) + 1, MPI_CHAR, 0, 1, MPI_COMM_WORLD);
    }
}

void run_mpi_hello_world_async() {
    // Get the rank of the process
    int my_rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

    // Get the total number of processes
    int num_processes;
    MPI_Comm_size(MPI_COMM_WORLD, &num_processes);

    if (my_rank == 0) {
        // Process 0: Receive messages asynchronously from all other processes
        int num_other_processes = num_processes - 1;

        // Arrays to store buffers, requests, and flags for each receive operation
        std::vector<char[256]> buffers(num_other_processes);
        std::vector<MPI_Request> requests(num_other_processes);
        std::vector<int> flags(num_other_processes, 0);

        // Post all non-blocking receives
        for (int i = 1; i < num_processes; i++) {
            MPI_Irecv(buffers[i - 1], 256, MPI_CHAR, i, 0, MPI_COMM_WORLD, &requests[i - 1]);
        }

        // Verification iteration - keep checking until all messages are received
        int received_count = 0;
        while (received_count < num_other_processes) {
            for (int i = 0; i < num_other_processes; i++) {
                if (flags[i] == 0) {  // If not yet received
                    MPI_Test(&requests[i], &flags[i], MPI_STATUS_IGNORE);
                    if (flags[i]) {  // Message received
                        received_count++;
                    }
                }
            }
        }

        // Build the final result string from all received messages
        std::string result = "";
        for (int i = 0; i < num_other_processes; i++) {
            result += buffers[i];
            if (i < num_other_processes - 1) {
                result += "\n";
            }
        }

        // Display the final concatenated string
        std::cout << "Process 0 received all async messages:\n" << result << std::endl;
    } else {
        // Other processes: Send a message asynchronously to process 0
        char message[256];
        sprintf(message, "Hello from id %d", my_rank);

        MPI_Request request;
        MPI_Isend(message, strlen(message) + 1, MPI_CHAR, 0, 0, MPI_COMM_WORLD, &request);

        // Wait for the send to complete
        int flag = 0;
        while (!flag) {
            MPI_Test(&request, &flag, MPI_STATUS_IGNORE);
        }
    }
}

void run_vector_addition() {
    // Get the rank of the process
    int my_rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

    // Get the total number of processes
    int num_processes;
    MPI_Comm_size(MPI_COMM_WORLD, &num_processes);

    const int VECTOR_SIZE = 100;
    int segment_size = VECTOR_SIZE / num_processes;

    if (my_rank == 0) {
        // Process 0: Initialize vectors A and B
        std::vector<int> A(VECTOR_SIZE);
        std::vector<int> B(VECTOR_SIZE);
        std::vector<int> C(VECTOR_SIZE);

        // Initialize vectors with some values
        for (int i = 0; i < VECTOR_SIZE; i++) {
            A[i] = i;
            B[i] = i * 2;
        }

        // Send segments to other processes
        for (int i = 1; i < num_processes; i++) {
            int start_idx = i * segment_size;
            MPI_Send(&A[start_idx], segment_size, MPI_INT, i, 0, MPI_COMM_WORLD);
            MPI_Send(&B[start_idx], segment_size, MPI_INT, i, 1, MPI_COMM_WORLD);
        }

        // Process 0 computes its own segment (first segment)
        for (int i = 0; i < segment_size; i++) {
            C[i] = A[i] + B[i];
        }

        // Receive computed segments from other processes
        for (int i = 1; i < num_processes; i++) {
            int start_idx = i * segment_size;
            MPI_Recv(&C[start_idx], segment_size, MPI_INT, i, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        // Display the result
        std::cout << "Vector addition completed. First 10 elements of C:\n";
        for (int i = 0; i < 10; i++) {
            std::cout << "C[" << i << "] = " << C[i] << " (A[" << i << "] + B[" << i << "] = " << A[i] << " + " << B[i] << ")\n";
        }
        std::cout << "...\n";
        std::cout << "Last 10 elements of C:\n";
        for (int i = VECTOR_SIZE - 10; i < VECTOR_SIZE; i++) {
            std::cout << "C[" << i << "] = " << C[i] << " (A[" << i << "] + B[" << i << "] = " << A[i] << " + " << B[i] << ")\n";
        }
    } else {
        // Other processes: Receive segments, compute, and send back
        std::vector<int> segment_A(segment_size);
        std::vector<int> segment_B(segment_size);
        std::vector<int> segment_C(segment_size);

        // Receive segments from process 0
        MPI_Recv(segment_A.data(), segment_size, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(segment_B.data(), segment_size, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // Compute C = A + B for this segment
        for (int i = 0; i < segment_size; i++) {
            segment_C[i] = segment_A[i] + segment_B[i];
        }

        // Send the result back to process 0
        MPI_Send(segment_C.data(), segment_size, MPI_INT, 0, 2, MPI_COMM_WORLD);
    }
}



void run_vector_addition_collective() {
    // Get the rank of the process
    int my_rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

    // Get the total number of processes
    int num_processes;
    MPI_Comm_size(MPI_COMM_WORLD, &num_processes);

    const int VECTOR_SIZE = 100;
    int segment_size = VECTOR_SIZE / num_processes;

    // All processes need their own segment buffers
    std::vector<int> segment_A(segment_size);
    std::vector<int> segment_B(segment_size);
    std::vector<int> segment_C(segment_size);

    std::vector<int> A, B, C;

    if (my_rank == 0) {
        // Process 0: Initialize vectors A and B
        A.resize(VECTOR_SIZE);
        B.resize(VECTOR_SIZE);
        C.resize(VECTOR_SIZE);

        // Initialize vectors with some values
        for (int i = 0; i < VECTOR_SIZE; i++) {
            A[i] = i;
            B[i] = i * 2;
        }
    }

    // Scatter A and B from process 0 to all processes
    MPI_Scatter(my_rank == 0 ? A.data() : nullptr, segment_size, MPI_INT,
                segment_A.data(), segment_size, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(my_rank == 0 ? B.data() : nullptr, segment_size, MPI_INT,
                segment_B.data(), segment_size, MPI_INT, 0, MPI_COMM_WORLD);

    // All processes compute their segment: C = A + B
    for (int i = 0; i < segment_size; i++) {
        segment_C[i] = segment_A[i] + segment_B[i];
    }

    // Gather all computed segments back to process 0
    MPI_Gather(segment_C.data(), segment_size, MPI_INT,
               my_rank == 0 ? C.data() : nullptr, segment_size, MPI_INT, 0, MPI_COMM_WORLD);

    if (my_rank == 0) {
        // Display the result
        std::cout << "Vector addition (collective) completed. First 10 elements of C:\n";
        for (int i = 0; i < 10; i++) {
            std::cout << "C[" << i << "] = " << C[i] << " (A[" << i << "] + B[" << i << "] = " << A[i] << " + " << B[i] << ")\n";
        }
        std::cout << "...\n";
        std::cout << "Last 10 elements of C:\n";
        for (int i = VECTOR_SIZE - 10; i < VECTOR_SIZE; i++) {
            std::cout << "C[" << i << "] = " << C[i] << " (A[" << i << "] + B[" << i << "] = " << A[i] << " + " << B[i] << ")\n";
        }
    }
}


int main(int argc, char** argv) {
    // Initialize the MPI environment
    MPI_Init(NULL, NULL);

    // Run the main logic
    run_mpi_hello_world();
    //run_mpi_hello_world_async();
    //run_vector_addition();
    //run_vector_addition_collective();

    std::cout<<"Finished";

    // Finalize the MPI environment
    MPI_Finalize();
    return 0;
}