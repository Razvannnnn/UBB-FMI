#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <omp.h>

#define N 1000000
#define MATRIX_SIZE 1000

// prefix_sums=(x1, x1+x2, x1+x2+x3,..., x1+x2+...+Xn)
void prefix_sums(double *input, double *output, int size)
{
    output[0] = input[0];
    for (int i = 1; i < size; i++)
    {
        output[i] = output[i - 1] + input[i];
    }
}

double **allocate_matrix(int rows, int cols)
{
    double **mat = (double **)malloc(rows * sizeof(double *));
    for (int i = 0; i < rows; i++)
    {
        mat[i] = (double *)malloc(cols * sizeof(double));
    }
    return mat;
}

void free_matrix(double **mat, int rows)
{
    for (int i = 0; i < rows; i++)
    {
        free(mat[i]);
    }
    free(mat);
}

void init_matrix(double **mat, int rows, int cols, double value)
{
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            mat[i][j] = value + i * cols + j;
        }
    }
}

void vector_ops_with_schedule(double *a, double *b, double *sum, double *prod, int size, const char *schedule_type)
{
    double start, end;

    start = omp_get_wtime();
    if (strcmp(schedule_type, "static") == 0)
    {
#pragma omp parallel for schedule(static)
        for (int i = 0; i < size; i++)
        {
            sum[i] = a[i] + b[i];
        }
    }
    else if (strcmp(schedule_type, "dynamic") == 0)
    {
#pragma omp parallel for schedule(dynamic)
        for (int i = 0; i < size; i++)
        {
            sum[i] = a[i] + b[i];
        }
    }
    else if (strcmp(schedule_type, "guided") == 0)
    {
#pragma omp parallel for schedule(guided)
        for (int i = 0; i < size; i++)
        {
            sum[i] = a[i] + b[i];
        }
    }
    end = omp_get_wtime();
    printf("Addition (%s): %.6f seconds\n", schedule_type, end - start);

    start = omp_get_wtime();
    if (strcmp(schedule_type, "static") == 0)
    {
#pragma omp parallel for schedule(static)
        for (int i = 0; i < size; i++)
        {
            prod[i] = a[i] * b[i];
        }
    }
    else if (strcmp(schedule_type, "dynamic") == 0)
    {
#pragma omp parallel for schedule(dynamic)
        for (int i = 0; i < size; i++)
        {
            prod[i] = a[i] * b[i];
        }
    }
    else if (strcmp(schedule_type, "guided") == 0)
    {
#pragma omp parallel for schedule(guided)
        for (int i = 0; i < size; i++)
        {
            prod[i] = a[i] * b[i];
        }
    }
    end = omp_get_wtime();
    printf("Multiplication (%s): %.6f seconds\n", schedule_type, end - start);
}

void vector_ops_nowait(double *a, double *b, double *sum, double *prod, int size)
{
    double start, end;

    start = omp_get_wtime();
    {
#pragma omp for schedule(static) nowait
        for (int i = 0; i < size; i++)
        {
            sum[i] = a[i] + b[i];
        }
#pragma omp for schedule(static) nowait
        for (int i = 0; i < size; i++)
        {
            prod[i] = a[i] * b[i];
        }
    }
    end = omp_get_wtime();
    printf("Both operations with no wait: %.6f seconds\n", end - start);
}

double scalar_product_critical(double *a, double *b, int size)
{
    double result = 0.0;
    double start, end;

    start = omp_get_wtime();
#pragma omp parallel for
    for (int i = 0; i < size; i++)
    {
        double temp = a[i] * b[i];
#pragma omp critical
        {
            result += temp;
        }
    }
    end = omp_get_wtime();
    printf("Scalar product (critical): %.6f seconds, result = %.2f\n", end - start, result);
    return result;
}

double scalar_product_atomic(double *a, double *b, int size)
{
    double result = 0.0;
    double start, end;

    start = omp_get_wtime();
#pragma omp parallel for
    for (int i = 0; i < size; i++)
    {
        double temp = a[i] * b[i];
#pragma omp atomic
        result += temp;
    }
    end = omp_get_wtime();
    printf("Scalar product (atomic): %.6f seconds, result = %.2f\n", end - start, result);
    return result;
}

double scalar_product_reduction(double *a, double *b, int size)
{
    double result = 0.0;
    double start, end;

    start = omp_get_wtime();
#pragma omp parallel for reduction(+ : result)
    for (int i = 0; i < size; i++)
    {
        result += a[i] * b[i];
    }
    end = omp_get_wtime();
    printf("Scalar product (reduction): %.6f seconds, result = %.2f\n", end - start, result);
    return result;
}

void matrix_add_collapse(double **mat1, double **mat2, double **result, int rows, int cols)
{
    double start, end;

    start = omp_get_wtime();
#pragma omp parallel for collapse(2)
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            result[i][j] = mat1[i][j] + mat2[i][j];
        }
    }
    end = omp_get_wtime();
    printf("Matrix addition (collapse): %.6f seconds", end - start);
}

void parallel_sections(double **mat1, double **mat2, double **mat_result,
                       double *vec1, double *vec2, double *vec_result,
                       double *prefix_input, double *prefix_output,
                       int mat_size, int vec_size, int prefix_size)
{
    double start, end;

    start = omp_get_wtime();
#pragma omp parallel sections
    {
#pragma omp section
        {
            // Section 1: Matrix addition
            for (int i = 0; i < mat_size; i++)
            {
                for (int j = 0; j < mat_size; j++)
                {
                    mat_result[i][j] = mat1[i][j] + mat2[i][j];
                }
            }
            printf("  Section 1 (matrix addition) completed by thread %d\n", omp_get_thread_num());
        }

#pragma omp section
        {
            // Section 2: Vector addition
            for (int i = 0; i < vec_size; i++)
            {
                vec_result[i] = vec1[i] + vec2[i];
            }
            printf("  Section 2 (vector addition) completed by thread %d\n", omp_get_thread_num());
        }

#pragma omp section
        {
            // Section 3: Prefix sums
            prefix_sums(prefix_input, prefix_output, prefix_size);
            printf("  Section 3 (prefix sums) completed by thread %d\n", omp_get_thread_num());
        }
    }
    end = omp_get_wtime();
    printf("  All sections completed in: %.6f seconds\n", end - start);
}

int main(int argc, char **argv)
{
    printf("Number of threads: %d\n\n", omp_get_max_threads());

    double *a, *b, *sum, *prod;
    a = (double *)malloc(N * sizeof(double));
    b = (double *)malloc(N * sizeof(double));
    sum = (double *)malloc(N * sizeof(double));
    prod = (double *)malloc(N * sizeof(double));

    for (int i = 0; i < N; i++)
    {
        a[i] = (double)(i % 100);
        b[i] = (double)((i % 100) + 1);
    }

    printf("====Vector Addition and Multiplication====\n");
    vector_ops_with_schedule(a, b, sum, prod, N, "static");
    vector_ops_with_schedule(a, b, sum, prod, N, "dynamic");
    vector_ops_with_schedule(a, b, sum, prod, N, "guided");

    printf("==== Both Vector Addition and Multiplication with nowait====\n");
    vector_ops_nowait(a, b, sum, prod, N);
    printf("==== Scalar product ====\n");
    scalar_product_critical(a, b, N);
    scalar_product_atomic(a, b, N);
    scalar_product_reduction(a, b, N);

    printf("==== Matrix addition with Collapse ====\n");
    double **mat1 = allocate_matrix(MATRIX_SIZE, MATRIX_SIZE);
    double **mat2 = allocate_matrix(MATRIX_SIZE, MATRIX_SIZE);
    double **mat_result = allocate_matrix(MATRIX_SIZE, MATRIX_SIZE);

    init_matrix(mat1, MATRIX_SIZE, MATRIX_SIZE, 1.0);
    init_matrix(mat2, MATRIX_SIZE, MATRIX_SIZE, 2.0);

    matrix_add_collapse(mat1, mat2, mat_result, MATRIX_SIZE, MATRIX_SIZE);

    printf("=== Parallel Sections === \n");
    int section_vec_size = 10000;
    int prefix_size = 10000;

    double *vec1 = (double *)malloc(section_vec_size * sizeof(double));
    double *vec2 = (double *)malloc(section_vec_size * sizeof(double));
    double *vec_result = (double *)malloc(section_vec_size * sizeof(double));
    double *prefix_input = (double *)malloc(prefix_size * sizeof(double));
    double *prefix_output = (double *)malloc(prefix_size * sizeof(double));

    for (int i = 0; i < section_vec_size; i++)
    {
        vec1[i] = i;
        vec2[i] = i + 1;
    }

    for (int i = 0; i < prefix_size; i++)
    {
        prefix_input[i] = 1.0;
    }

    parallel_sections(mat1, mat2, mat_result, vec1, vec2, vec_result, prefix_input, prefix_output, MATRIX_SIZE, section_vec_size, prefix_size);

    free(a);
    free(b);
    free(sum);
    free(prod);
    free_matrix(mat1, MATRIX_SIZE);
    free_matrix(mat2, MATRIX_SIZE);
    free_matrix(mat_result, MATRIX_SIZE);
    free(vec1);
    free(vec2);
    free(vec_result);
    free(prefix_input);
    free(prefix_output);

    return 0;
}
